package com.github.ppodgorsek.juncacher.varnish.strategy.impl;

import com.github.ppodgorsek.juncacher.model.impl.IdentifiedInvalidationEntry;
import com.github.ppodgorsek.juncacher.strategy.InvalidationStrategy;
import com.github.ppodgorsek.juncacher.varnish.strategy.AbstractVarnishUrlStrategy;

/**
 * URL strategy that replaces a placeholder in a URL by an invalidation entry's ID.
 *
 * @since 1.0
 * @author Paul Podgorsek
 */
public class IdentifiedVarnishUrlStrategy
		extends AbstractVarnishUrlStrategy<IdentifiedInvalidationEntry>
		implements InvalidationStrategy<IdentifiedInvalidationEntry> {

	private static final String DEFAULT_PLACEHOLDER = "ENTRY_ID";

	/**
	 * The placeholder that will be replaced in the URL by the entry's ID.
	 */
	private String placeholder = DEFAULT_PLACEHOLDER;

	@Override
	protected String getUpdatedUrl(final IdentifiedInvalidationEntry entry, final String url) {
		return url.replaceAll(placeholder, entry.getReferenceId());
	}

	public String getPlaceholder() {
		return placeholder;
	}

	public void setPlaceholder(final String newPlaceholder) {
		placeholder = newPlaceholder;
	}

}
