package com.github.ppodgorsek.juncacher.varnish.http;

import java.net.URI;

import org.apache.http.client.methods.HttpRequestBase;

/**
 * HTTP method used to send PURGE requests.
 *
 * @since 1.0
 * @author ppodgorsek
 */
public class HttpPurge extends HttpRequestBase {

	private static final String METHOD_NAME = "PURGE";

	/**
	 * Default constructor.
	 */
	public HttpPurge() {
		super();
	}

	/**
	 * Constructor allowing to set the target URI.
	 *
	 * @param uri
	 *            The target URI.
	 */
	public HttpPurge(final URI uri) {
		super();
		setURI(uri);
	}

	/**
	 * Constructor allowing to set the target URI.
	 *
	 * @param uri
	 *            The target URI.
	 */
	public HttpPurge(final String uri) {
		super();
		setURI(URI.create(uri));
	}

	@Override
	public String getMethod() {
		return METHOD_NAME;
	}

}
