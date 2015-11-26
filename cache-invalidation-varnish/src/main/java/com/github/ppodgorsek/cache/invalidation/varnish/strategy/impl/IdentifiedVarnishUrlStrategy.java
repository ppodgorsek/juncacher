package com.github.ppodgorsek.cache.invalidation.varnish.strategy.impl;

import com.github.ppodgorsek.cache.invalidation.model.impl.IdentifiedInvalidationEntry;
import com.github.ppodgorsek.cache.invalidation.varnish.strategy.AbstractVarnishUrlStrategy;
import com.github.ppodgorsek.cache.invalidation.varnish.strategy.VarnishUrlStrategy;

/**
 * URL strategy that replaces a placeholder in a URL by an invalidation entry's ID.
 *
 * @since 1.0
 * @author Paul Podgorsek
 */
public class IdentifiedVarnishUrlStrategy
		extends AbstractVarnishUrlStrategy<IdentifiedInvalidationEntry>
		implements VarnishUrlStrategy<IdentifiedInvalidationEntry> {

	private static final String DEFAULT_PLACEHOLDER = "ENTRY_ID";

	/**
	 * The placeholder that will be replaced in the URL by the entry's ID.
	 */
	private String placeholder = DEFAULT_PLACEHOLDER;

	@Override
	protected String getUpdatedUrl(final IdentifiedInvalidationEntry entry, final String url) {
		return url.replaceAll(placeholder, entry.getId());
	}

	public String getPlaceholder() {
		return placeholder;
	}

	public void setPlaceholder(final String newPlaceholder) {
		placeholder = newPlaceholder;
	}

}
