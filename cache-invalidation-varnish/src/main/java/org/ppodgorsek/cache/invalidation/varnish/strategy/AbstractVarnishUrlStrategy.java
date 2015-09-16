package org.ppodgorsek.cache.invalidation.varnish.strategy;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.methods.GetMethod;
import org.ppodgorsek.cache.invalidation.model.InvalidationEntry;
import org.ppodgorsek.cache.invalidation.varnish.http.HttpBanMethod;
import org.ppodgorsek.cache.invalidation.varnish.http.HttpPurgeMethod;
import org.springframework.beans.factory.annotation.Required;

/**
 * Holder containing the URLs for an invalidation entry type's BAN and PURGE requests. Once these requests are sent, the reload URLs should be used to make sure
 * Varnish has been properly re-populated.
 *
 * @author Paul Podgorsek
 */
public abstract class AbstractVarnishUrlStrategy<T extends InvalidationEntry> implements VarnishUrlStrategy<T> {

	private static final String HOST_HEADER_NAME = "host";

	private List<String> banUrls;
	private List<String> getUrls;
	private List<String> purgeUrls;

	/**
	 * The host can be used when going through a proxy in order for Varnish to hash the objects using the proper host. See
	 * {@link http://tools.ietf.org/html/rfc7230#section-5.4}
	 */
	private String host;

	@Override
	public List<HttpBanMethod> getBanMethods(final T entry) {

		final List<HttpBanMethod> methods = new ArrayList<>();

		for (final String url : banUrls) {
			final HttpBanMethod method = getBanMethod(entry, url);
			methods.add(method);
		}

		return methods;
	}

	@Override
	public List<GetMethod> getGetMethods(final T entry) {

		final List<GetMethod> methods = new ArrayList<>();

		for (final String url : getUrls) {
			final GetMethod method = getGetMethod(entry, url);
			methods.add(method);
		}

		return methods;
	}

	@Override
	public List<HttpPurgeMethod> getPurgeMethods(final T entry) {

		final List<HttpPurgeMethod> methods = new ArrayList<>();

		for (final String url : purgeUrls) {
			final HttpPurgeMethod method = getPurgeMethod(entry, url);
			methods.add(method);
		}

		return methods;
	}

	protected abstract String getUpdatedUrl(T entry, String url);

	private HttpBanMethod getBanMethod(final T entry, final String url) {

		final String replacedUrl = getUpdatedUrl(entry, url);
		final HttpBanMethod method = new HttpBanMethod(replacedUrl);

		if (host != null) {
			method.addRequestHeader(HOST_HEADER_NAME, host);
		}

		return method;
	}

	private GetMethod getGetMethod(final T entry, final String url) {

		final String replacedUrl = getUpdatedUrl(entry, url);
		final GetMethod method = new GetMethod(replacedUrl);

		if (host != null) {
			method.addRequestHeader(HOST_HEADER_NAME, host);
		}

		return method;
	}

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
