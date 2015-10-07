package org.ppodgorsek.cache.invalidation.varnish.strategy;

import java.util.List;

import org.apache.commons.httpclient.methods.GetMethod;
import org.ppodgorsek.cache.invalidation.model.InvalidationEntry;
import org.ppodgorsek.cache.invalidation.varnish.http.HttpBanMethod;
import org.ppodgorsek.cache.invalidation.varnish.http.HttpPurgeMethod;

/**
 * Strategy used to determines which URLs should be banned/purged/reloaded in Varnish.
 *
 * @since 1.0
 * @author Paul Podgorsek
 */
public interface VarnishUrlStrategy<T extends InvalidationEntry> {

	/**
	 * Return the list of HTTP BAN methods to ban an entry from Varnish.
	 *
	 * @param entry
	 *            The invalidation entry, mustn't be {@code null}.
	 * @return The list of HTTP BAN methods related to the provided entry, or an empty list if there
	 *         are none.
	 */
	List<HttpBanMethod> getBanMethods(T entry);

	/**
	 * Return the list of HTTP GET methods to reload an entry in Varnish.
	 *
	 * @param entry
	 *            The invalidation entry, mustn't be {@code null}.
	 * @return The list of HTTP GET methods related to the provided entry, or an empty list if there
	 *         are none.
	 */
	List<GetMethod> getGetMethods(T entry);

	/**
	 * Return the list of HTTP PURGE methods to purge an entry from Varnish.
	 *
	 * @param entry
	 *            The invalidation entry, mustn't be {@code null}.
	 * @return The list of HTTP PURGE methods related to the provided entry, or an empty list if
	 *         there are none.
	 */
	List<HttpPurgeMethod> getPurgeMethods(T entry);

}
