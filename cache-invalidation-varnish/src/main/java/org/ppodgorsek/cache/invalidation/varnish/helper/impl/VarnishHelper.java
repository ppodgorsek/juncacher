package org.ppodgorsek.cache.invalidation.varnish.helper.impl;

import java.io.IOException;
import java.net.ConnectException;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.ppodgorsek.cache.invalidation.exception.InvalidationException;
import org.ppodgorsek.cache.invalidation.helper.AbstractChainedInvalidationHelper;
import org.ppodgorsek.cache.invalidation.helper.InvalidationHelper;
import org.ppodgorsek.cache.invalidation.model.InvalidationEntry;
import org.ppodgorsek.cache.invalidation.varnish.strategy.VarnishUrlStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * Helper used to send invalidation requests to Varnish.
 *
 * @since 1.0
 * @author Paul Podgorsek
 */
public class VarnishHelper<T extends InvalidationEntry> extends AbstractChainedInvalidationHelper<T>
		implements InvalidationHelper<T> {

	private static final Logger LOGGER = LoggerFactory.getLogger(VarnishHelper.class);

	private static final int MAX_TRIES = 5;

	private HttpClient httpClient;

	/**
	 * The maximum number of times invalidation requests will be sent to Varnish before giving up.
	 */
	private int maxTries = MAX_TRIES;

	private Map<Class<T>, VarnishUrlStrategy<T>> varnishUrlStrategies;

	@Override
	protected void invalidateEntry(final T entry) throws InvalidationException {

		final VarnishUrlStrategy<T> varnishUrlStrategy = varnishUrlStrategies.get(entry.getClass());

		if (varnishUrlStrategy == null) {
			LOGGER.info("No URL strategy found for entry {}", entry);
		}
		else {
			try {
				sendHttpMethods(varnishUrlStrategy.getBanMethods(entry));
				sendHttpMethods(varnishUrlStrategy.getPurgeMethods(entry));
				sendHttpMethods(varnishUrlStrategy.getGetMethods(entry));
			}
			catch (final ConnectException e) {
				throw new InvalidationException(
						"Impossible to evict entry " + entry + " from Varnish", e);
			}
		}
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
	private void sendHttpMethods(final List<? extends HttpMethodBase> methods)
			throws ConnectException {

		for (final HttpMethodBase method : methods) {
			sendHttpMethod(method);
		}
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

	public Map<Class<T>, VarnishUrlStrategy<T>> getVarnishUrlStrategies() {
		return varnishUrlStrategies;
	}

	@Required
	public void setVarnishUrlStrategies(final Map<Class<T>, VarnishUrlStrategy<T>> strategies) {
		varnishUrlStrategies = strategies;
	}

}
