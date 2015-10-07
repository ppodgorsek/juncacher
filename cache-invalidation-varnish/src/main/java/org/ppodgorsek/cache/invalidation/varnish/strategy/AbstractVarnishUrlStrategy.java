package org.ppodgorsek.cache.invalidation.varnish.strategy;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.methods.GetMethod;
import org.ppodgorsek.cache.invalidation.model.InvalidationEntry;
import org.ppodgorsek.cache.invalidation.varnish.http.HttpBanMethod;
import org.ppodgorsek.cache.invalidation.varnish.http.HttpPurgeMethod;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

/**
 * Holder containing the URLs for an invalidation entry type's BAN, GET and PURGE requests. Once the
 * BAN and PURGE requests have been sent, the GET URLs should be used to properly re-populate
 * Varnish.
 *
 * @since 1.0
 * @author Paul Podgorsek
 */
public abstract class AbstractVarnishUrlStrategy<T extends InvalidationEntry>
		implements VarnishUrlStrategy<T> {

	private static final String HOST_HEADER_NAME = "host";

	private List<String> banUrls;
	private List<String> getUrls;
	private List<String> purgeUrls;

	/**
	 * The host can be used when going through a proxy in order for Varnish to hash the objects
	 * using the proper host. See {@link http://tools.ietf.org/html/rfc7230#section-5.4}
	 */
	private String host;

	@Override
	public List<HttpBanMethod> getBanMethods(final T entry) {

		Assert.notNull(entry, "Entry is required");

		final List<HttpBanMethod> methods = new ArrayList<>();

		for (final String url : banUrls) {
			final HttpBanMethod method = getBanMethod(entry, url);
			methods.add(method);
		}

		return methods;
	}

	@Override
	public List<GetMethod> getGetMethods(final T entry) {

		Assert.notNull(entry, "Entry is required");

		final List<GetMethod> methods = new ArrayList<>();

		for (final String url : getUrls) {
			final GetMethod method = getGetMethod(entry, url);
			methods.add(method);
		}

		return methods;
	}

	@Override
	public List<HttpPurgeMethod> getPurgeMethods(final T entry) {

		Assert.notNull(entry, "Entry is required");

		final List<HttpPurgeMethod> methods = new ArrayList<>();

		for (final String url : purgeUrls) {
			final HttpPurgeMethod method = getPurgeMethod(entry, url);
			methods.add(method);
		}

		return methods;
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
	 * Generate a HTTP BAN method according to a URL and information contained in an invalidation
	 * entry.
	 *
	 * @param entry
	 *            The invalidation entry.
	 * @param url
	 *            The URL.
	 * @return The HTTP BAN method corresponding to the provided URL and entry.
	 */
	private HttpBanMethod getBanMethod(final T entry, final String url) {

		final String replacedUrl = getUpdatedUrl(entry, url);
		final HttpBanMethod method = new HttpBanMethod(replacedUrl);

		if (host != null) {
			method.addRequestHeader(HOST_HEADER_NAME, host);
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
	private GetMethod getGetMethod(final T entry, final String url) {

		final String replacedUrl = getUpdatedUrl(entry, url);
		final GetMethod method = new GetMethod(replacedUrl);

		if (host != null) {
			method.addRequestHeader(HOST_HEADER_NAME, host);
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
	private HttpPurgeMethod getPurgeMethod(final T entry, final String url) {

		final String replacedUrl = getUpdatedUrl(entry, url);
		final HttpPurgeMethod method = new HttpPurgeMethod(replacedUrl);

		if (host != null) {
			method.addRequestHeader(HOST_HEADER_NAME, host);
		}

		return method;
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
