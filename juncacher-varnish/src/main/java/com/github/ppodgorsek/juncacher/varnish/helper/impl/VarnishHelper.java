package com.github.ppodgorsek.juncacher.varnish.helper.impl;

import java.io.IOException;
import java.net.ConnectException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import com.github.ppodgorsek.juncacher.exception.InvalidationException;
import com.github.ppodgorsek.juncacher.helper.AbstractChainedInvalidationHelper;
import com.github.ppodgorsek.juncacher.helper.InvalidationHelper;
import com.github.ppodgorsek.juncacher.model.InvalidationEntry;
import com.github.ppodgorsek.juncacher.varnish.strategy.VarnishUrlStrategy;

/**
 * Helper used to send invalidation requests to Varnish.
 *
 * @since 1.0
 * @author Paul Podgorsek
 */
public class VarnishHelper<T extends InvalidationEntry>
		extends AbstractChainedInvalidationHelper<T, VarnishUrlStrategy<T>>
		implements InvalidationHelper<T> {

	private static final Logger LOGGER = LoggerFactory.getLogger(VarnishHelper.class);

	private static final int DEFAULT_MAX_TRIES = 5;

	private HttpClient httpClient;

	/**
	 * The maximum number of times invalidation requests will be sent to Varnish before giving up.
	 */
	private int maxTries = DEFAULT_MAX_TRIES;

	@Override
	protected void invalidateEntry(final T entry, final VarnishUrlStrategy<T> strategy)
			throws InvalidationException {

		try {
			sendHttpMethods(strategy.getBanMethods(entry));
			sendHttpMethods(strategy.getPurgeMethods(entry));
			sendHttpMethods(strategy.getGetMethods(entry));
		}
		catch (final ConnectException e) {
			throw new InvalidationException("Impossible to evict entry " + entry + " from Varnish",
					e);
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
	protected void sendHttpMethod(final HttpRequestBase method) throws ConnectException {

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

}
