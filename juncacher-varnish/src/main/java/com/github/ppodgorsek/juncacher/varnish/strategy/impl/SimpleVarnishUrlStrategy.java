package com.github.ppodgorsek.juncacher.varnish.strategy.impl;

import com.github.ppodgorsek.juncacher.model.InvalidationEntry;
import com.github.ppodgorsek.juncacher.varnish.strategy.AbstractVarnishUrlStrategy;
import com.github.ppodgorsek.juncacher.varnish.strategy.VarnishUrlStrategy;

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
