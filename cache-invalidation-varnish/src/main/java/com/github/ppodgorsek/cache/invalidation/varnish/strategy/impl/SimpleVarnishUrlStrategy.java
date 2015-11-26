package com.github.ppodgorsek.cache.invalidation.varnish.strategy.impl;

import com.github.ppodgorsek.cache.invalidation.model.InvalidationEntry;
import com.github.ppodgorsek.cache.invalidation.varnish.strategy.AbstractVarnishUrlStrategy;
import com.github.ppodgorsek.cache.invalidation.varnish.strategy.VarnishUrlStrategy;

/**
 * URL strategy that does no transformation to URLs.
 *
 * @since 1.0
 * @author Paul Podgorsek
 */
public class SimpleVarnishUrlStrategy extends AbstractVarnishUrlStrategy<InvalidationEntry>
		implements VarnishUrlStrategy<InvalidationEntry> {

	@Override
	protected String getUpdatedUrl(final InvalidationEntry entry, final String url) {
		return url;
	}

}
