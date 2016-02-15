package com.github.ppodgorsek.juncacher.spring.helper.impl;

import com.github.ppodgorsek.juncacher.exception.InvalidationException;
import com.github.ppodgorsek.juncacher.helper.AbstractChainedInvalidationHelper;
import com.github.ppodgorsek.juncacher.helper.InvalidationHelper;
import com.github.ppodgorsek.juncacher.model.InvalidationEntry;
import com.github.ppodgorsek.juncacher.spring.strategy.CacheManagerStrategy;

/**
 * Helper used to evict entries from a Spring Cache Manager.
 *
 * @since 1.0
 * @author Paul Podgorsek
 */
public class CacheManagerHelper<T extends InvalidationEntry>
		extends AbstractChainedInvalidationHelper<T, CacheManagerStrategy<T>>
		implements InvalidationHelper<T> {

	@Override
	protected void invalidateEntry(final T entry, final CacheManagerStrategy<T> strategy)
			throws InvalidationException {

		strategy.evict(entry);
	}

}
