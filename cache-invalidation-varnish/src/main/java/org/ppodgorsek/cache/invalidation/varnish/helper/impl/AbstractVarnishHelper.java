package org.ppodgorsek.cache.invalidation.varnish.helper.impl;

import java.io.IOException;
import java.net.ConnectException;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.URIException;
import org.ppodgorsek.cache.invalidation.exception.InvalidationException;
import org.ppodgorsek.cache.invalidation.helper.InvalidationHelper;
import org.ppodgorsek.cache.invalidation.model.InvalidationEntry;
import org.ppodgorsek.cache.invalidation.varnish.http.HttpBanMethod;
import org.ppodgorsek.cache.invalidation.varnish.http.HttpPurgeMethod;
import org.ppodgorsek.cache.invalidation.varnish.model.VarnishUrlHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * Helper used to send requests to Varnish.
 * 
 * @author Paul Podgorsek
 */
public abstract class AbstractVarnishHelper implements InvalidationHelper {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractVarnishHelper.class);

	private static final int MAX_TRIES = 3;

	private HttpClient httpClient;

	private Map<Class<? extends InvalidationEntry>, VarnishUrlHolder> urlHolders;

	/**
	 * Default constructor.
	 */
	public AbstractVarnishHelper() {
		super();
	}

	@Override
	public void invalidateEntry(final InvalidationEntry entry) throws InvalidationException {

		VarnishUrlHolder urlHolder = urlHolders.get(entry.getClass());

		if (urlHolder != null) {
			try {
				ban(entry, urlHolder.getBanUrls());
				purge(entry, urlHolder.getPurgeUrls());
			}
			catch (ConnectException | URIException e) {
				throw new InvalidationException("Impossible to evict the entry from Varnish", e);
			}
		}
	}

	protected abstract HttpBanMethod getBanMethod(InvalidationEntry entry, String url);

	protected abstract HttpPurgeMethod getPurgeMethod(InvalidationEntry entry, String url);

	/**
	 * Sends a HTTP method. If the remote host can't be contacted, this method will retry several times before propagating the exception.
	 * 
	 * @param method
	 *            The HTTP method.
	 * @throws UnreachableHostException
	 *             An exception thrown if the remote host can't be contacted.
	 */
	protected void sendHttpMethod(final HttpMethodBase method) throws ConnectException {

		int tries = 0;
		boolean success = false;

		do {
			tries++;

			try {
				final int status = httpClient.executeMethod(method);

				LOGGER.debug("Response status of the method execution: {}", status);

				success = true;
			}
			catch (final HttpException e) {
				LOGGER.warn("Impossible to execute the request: {}", e.getMessage());
			}
			catch (final IOException e) {
				LOGGER.warn("Impossible to execute the request: {}", e.getMessage());
			}
			finally {
				method.releaseConnection();
			}
		}
		while (!success && tries < MAX_TRIES);

		if (!success) {
			throw new ConnectException();
		}
	}

	private void ban(final InvalidationEntry entry, final List<String> banUrls) throws ConnectException, URIException {

		for (String url : banUrls) {
			HttpBanMethod method = getBanMethod(entry, url);

			sendHttpMethod(method);
		}
	}

	private void purge(final InvalidationEntry entry, final List<String> purgeUrls) throws ConnectException, URIException {

		for (String url : purgeUrls) {
			HttpPurgeMethod method = getPurgeMethod(entry, url);

			sendHttpMethod(method);
		}
	}

	@Required
	public void setHttpClient(final HttpClient client) {
		httpClient = client;
	}

	@Required
	public void setUrlHolders(final Map<Class<? extends InvalidationEntry>, VarnishUrlHolder> holders) {
		urlHolders = holders;
	}

}
