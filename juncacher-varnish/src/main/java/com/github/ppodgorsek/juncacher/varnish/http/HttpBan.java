package com.github.ppodgorsek.juncacher.varnish.http;

import java.net.URI;

import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.client.methods.HttpRequestBase;

/**
 * HTTP method used to send BAN requests.
 *
 * @since 1.0
 * @author ppodgorsek
 */
@NotThreadSafe
public class HttpBan extends HttpRequestBase {

	private static final String METHOD_NAME = "BAN";

	/**
	 * Default constructor.
	 */
	public HttpBan() {
		super();
	}

	/**
	 * Constructor allowing to set the target URI.
	 *
	 * @param uri
	 *            The target URI.
	 */
	public HttpBan(final URI uri) {
		super();
		setURI(uri);
	}

	/**
	 * Constructor allowing to set the target URI.
	 *
	 * @param uri
	 *            The target URI.
	 */
	public HttpBan(final String uri) {
		super();
		setURI(URI.create(uri));
	}

	@Override
	public String getMethod() {
		return METHOD_NAME;
	}

}
