package com.github.ppodgorsek.juncacher.varnish.strategy;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import com.github.ppodgorsek.juncacher.exception.InvalidationException;
import com.github.ppodgorsek.juncacher.model.InvalidationEntry;
import com.github.ppodgorsek.juncacher.strategy.InvalidationStrategy;
import com.github.ppodgorsek.juncacher.varnish.http.HttpBan;
import com.github.ppodgorsek.juncacher.varnish.http.HttpPurge;

/**
 * Strategy used to determine which URLs should be banned/purged/reloaded in Varnish. It contains
 * the URLs for an invalidation entry type's BAN, GET and PURGE requests. Once the BAN and PURGE
 * requests have been sent, the GET URLs should be used to properly re-populate Varnish.
 *
 * @since 1.0
 * @author Paul Podgorsek
 */
public abstract class AbstractVarnishUrlStrategy<T extends InvalidationEntry>
		implements InvalidationStrategy<T> {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractVarnishUrlStrategy.class);

	private static final int DEFAULT_MAX_TRIES = 5;

	private static final String HOST_HEADER_NAME = "host";

	private HttpClient httpClient;

	/**
	 * The maximum number of times invalidation requests will be sent to Varnish before giving up.
	 */
	private int maxTries = DEFAULT_MAX_TRIES;

	private List<String> banUrls;
	private List<String> getUrls;
	private List<String> purgeUrls;

	/**
	 * The host can be used when going through a proxy in order for Varnish to hash the objects
	 * using the proper host. See {@link http://tools.ietf.org/html/rfc7230#section-5.4}
	 */
	private String host;

	@Override
	public void invalidate(final T entry) throws InvalidationException {

		try {
			sendHttpMethods(getBanMethods(entry));
			sendHttpMethods(getPurgeMethods(entry));
			sendHttpMethods(getGetMethods(entry));
		}
		catch (final ConnectException e) {
			throw new InvalidationException("Impossible to evict entry " + entry + " from Varnish",
					e);
		}
	}

	/**
	 * Transform a URL according to information contained in an invalidation entry.
	 *
	 * @param entry
	 *            The invalidation entry.
	 * @param url
	 *            The URL.
	 * @return The transformed URL, it can be the same as the input URL if no transformation was
	 *         applied.
	 */
	protected abstract String getUpdatedUrl(T entry, String url);

	/**
	 * Return the list of HTTP BAN methods to ban an entry from Varnish.
	 *
	 * @param entry
	 *            The invalidation entry, mustn't be {@code null}.
	 * @return The list of HTTP BAN methods related to the provided entry, or an empty list if there
	 *         are none.
	 */
	private List<HttpBan> getBanMethods(final T entry) {

		Assert.notNull(entry, "Entry is required");

		final List<HttpBan> methods = new ArrayList<>();

		for (final String url : banUrls) {
			final HttpBan method = getHttpBan(entry, url);
			methods.add(method);
		}

		return methods;
	}

	/**
	 * Return the list of HTTP GET methods to reload an entry in Varnish.
	 *
	 * @param entry
	 *            The invalidation entry, mustn't be {@code null}.
	 * @return The list of HTTP GET methods related to the provided entry, or an empty list if there
	 *         are none.
	 */
	private List<HttpGet> getGetMethods(final T entry) {

		Assert.notNull(entry, "Entry is required");

		final List<HttpGet> methods = new ArrayList<>();

		for (final String url : getUrls) {
			final HttpGet method = getHttpGet(entry, url);
			methods.add(method);
		}

		return methods;
	}

	/**
	 * Return the list of HTTP PURGE methods to purge an entry from Varnish.
	 *
	 * @param entry
	 *            The invalidation entry, mustn't be {@code null}.
	 * @return The list of HTTP PURGE methods related to the provided entry, or an empty list if
	 *         there are none.
	 */
	private List<HttpPurge> getPurgeMethods(final T entry) {

		Assert.notNull(entry, "Entry is required");

		final List<HttpPurge> methods = new ArrayList<>();

		for (final String url : purgeUrls) {
			final HttpPurge method = getHttpPurge(entry, url);
			methods.add(method);
		}

		return methods;
	}

	/**
	 * Generate a HTTP BAN method according to a URL and information contained in an invalidation
	 * entry.
	 *
	 * @param entry
	 *            The invalidation entry.
	 * @param url
	 *            The URL.
	 * @return The HTTP BAN method corresponding to the provided URL and entry.
	 */
	private HttpBan getHttpBan(final T entry, final String url) {

		final String replacedUrl = getUpdatedUrl(entry, url);
		final HttpBan method = new HttpBan(replacedUrl);

		if (host != null) {
			method.addHeader(HOST_HEADER_NAME, host);
		}

		return method;
	}

	/**
	 * Generate a HTTP GET method according to a URL and information contained in an invalidation
	 * entry.
	 *
	 * @param entry
	 *            The invalidation entry.
	 * @param url
	 *            The URL.
	 * @return The HTTP GET method corresponding to the provided URL and entry.
	 */
	private HttpGet getHttpGet(final T entry, final String url) {

		final String replacedUrl = getUpdatedUrl(entry, url);
		final HttpGet method = new HttpGet(replacedUrl);

		if (host != null) {
			method.addHeader(HOST_HEADER_NAME, host);
		}

		return method;
	}

	/**
	 * Generate a HTTP PURGE method according to a URL and information contained in an invalidation
	 * entry.
	 *
	 * @param entry
	 *            The invalidation entry.
	 * @param url
	 *            The URL.
	 * @return The HTTP PURGE method corresponding to the provided URL and entry.
	 */
	private HttpPurge getHttpPurge(final T entry, final String url) {

		final String replacedUrl = getUpdatedUrl(entry, url);
		final HttpPurge method = new HttpPurge(replacedUrl);

		if (host != null) {
			method.addHeader(HOST_HEADER_NAME, host);
		}

		return method;
	}

	/**
	 * Send a HTTP method. If the remote host can't be contacted, this method will retry several
	 * times before propagating the exception.
	 *
	 * @param method
	 *            The HTTP method.
	 * @throws ConnectException
	 *             An exception thrown if the remote host can't be contacted.
	 */
	private void sendHttpMethod(final HttpRequestBase method) throws ConnectException {

		int tries = 0;
		boolean success = false;

		do {
			tries++;

			try {
				final HttpResponse response = httpClient.execute(method);

				LOGGER.debug("Response status of the method execution: {}", response);

				success = true;
			}
			catch (final IOException e) {
				LOGGER.warn("Impossible to execute the request: {}", e.getMessage());
			}
			finally {
				method.releaseConnection();
			}
		}
		while (!success && tries < maxTries);

		if (!success) {
			throw new ConnectException();
		}
	}

	/**
	 * Send a list of HTTP methods to Varnish, one by one.
	 *
	 * @param methods
	 *            The HTTP methods.
	 * @throws ConnectException
	 *             An exception thrown if the communication with Varnish fails.
	 */
	private void sendHttpMethods(final List<? extends HttpRequestBase> methods)
			throws ConnectException {

		for (final HttpRequestBase method : methods) {
			sendHttpMethod(method);
		}
	}

	public List<String> getBanUrls() {
		return banUrls;
	}

	@Required
	public void setBanUrls(final List<String> urls) {
		banUrls = urls;
	}

	public List<String> getGetUrls() {
		return getUrls;
	}

	@Required
	public void setGetUrls(final List<String> urls) {
		getUrls = urls;
	}

	/**
	 * @return The HTTP client.
	 */
	public HttpClient getHttpClient() {
		return httpClient;
	}

	@Required
	public void setHttpClient(final HttpClient client) {
		httpClient = client;
	}

	/**
	 * @return The maximum number of tries.
	 */
	public int getMaxTries() {
		return maxTries;
	}

	/**
	 * Sets the maximum number of times invalidation requests will be sent to Varnish before giving
	 * up.
	 *
	 * @param newMaxTries
	 *            The new maximum of tries.
	 */
	public void setMaxTries(final int newMaxTries) {
		maxTries = newMaxTries;
	}

	public List<String> getPurgeUrls() {
		return purgeUrls;
	}

	@Required
	public void setPurgeUrls(final List<String> urls) {
		purgeUrls = urls;
	}

	public String getHost() {
		return host;
	}

	public void setHost(final String newHost) {
		host = newHost;
	}

}
