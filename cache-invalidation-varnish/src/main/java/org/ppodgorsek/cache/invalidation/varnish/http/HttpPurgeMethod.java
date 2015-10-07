package org.ppodgorsek.cache.invalidation.varnish.http;

import org.apache.commons.httpclient.HttpMethodBase;

/**
 * HTTP method used to send PURGE requests.
 *
 * @since 1.0
 * @author ppodgorsek
 */
public class HttpPurgeMethod extends HttpMethodBase {

	private static final String METHOD_NAME = "PURGE";

	/**
	 * Default constructor.
	 */
	public HttpPurgeMethod() {
		super();
		setFollowRedirects(true);
	}

	/**
	 * Constructor allowing to set the target URL.
	 *
	 * @param url
	 *            The target URL.
	 */
	public HttpPurgeMethod(final String url) {
		super(url);
		setFollowRedirects(true);
	}

	@Override
	public String getName() {
		return METHOD_NAME;
	}

}
