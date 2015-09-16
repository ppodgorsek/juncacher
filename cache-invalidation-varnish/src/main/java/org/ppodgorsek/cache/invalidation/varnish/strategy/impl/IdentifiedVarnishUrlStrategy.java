package org.ppodgorsek.cache.invalidation.varnish.strategy.impl;

import org.ppodgorsek.cache.invalidation.model.impl.IdentifiedInvalidationEntry;
import org.ppodgorsek.cache.invalidation.varnish.strategy.AbstractVarnishUrlStrategy;
import org.ppodgorsek.cache.invalidation.varnish.strategy.VarnishUrlStrategy;

/**
 * @author Paul Podgorsek
 */
public class IdentifiedVarnishUrlStrategy extends AbstractVarnishUrlStrategy<IdentifiedInvalidationEntry>
		implements VarnishUrlStrategy<IdentifiedInvalidationEntry> {

	private static final String DEFAULT_PLACEHOLDER = "ENTRY_ID";

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
