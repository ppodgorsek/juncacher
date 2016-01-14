package com.github.ppodgorsek.juncacher.varnish.http;

import org.apache.commons.httpclient.HttpMethodBase;

/**
 * HTTP method used to send BAN requests.
 *
 * @since 1.0
 * @author ppodgorsek
 */
public class HttpBanMethod extends HttpMethodBase {

	private static final String METHOD_NAME = "BAN";

	/**
	 * Default constructor.
	 */
	public HttpBanMethod() {
		super();
		setFollowRedirects(true);
	}

	/**
	 * Constructor allowing to set the target URL.
	 *
	 * @param url
	 *            The target URL.
	 */
	public HttpBanMethod(final String url) {
		super(url);
		setFollowRedirects(true);
	}

	@Override
	public String getName() {
		return METHOD_NAME;
	}

}
