package com.github.ppodgorsek.juncacher.spring.strategy.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;

import com.github.ppodgorsek.juncacher.exception.InvalidationException;
import com.github.ppodgorsek.juncacher.model.InvalidationEntry;
import com.github.ppodgorsek.juncacher.spring.strategy.AbstractCacheManagerInvalidationStrategy;
import com.github.ppodgorsek.juncacher.strategy.InvalidationStrategy;

/**
 * Simple strategy that clears whole cache regions when an entry needs to be invalidated.
 *
 * @since 1.0
 * @author Paul Podgorsek
 */
public class SimpleCacheManagerInvalidationStrategy
		extends AbstractCacheManagerInvalidationStrategy<InvalidationEntry>
		implements InvalidationStrategy<InvalidationEntry> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SimpleCacheManagerInvalidationStrategy.class);

	@Override
	public boolean canHandle(final InvalidationEntry entry) {
		return true;
	}

	@Override
	public void invalidate(final InvalidationEntry entry) throws InvalidationException {

		for (final String cacheName : getCacheNames()) {

			final Cache cache = getCacheManager().getCache(cacheName);

			if (cache != null) {
				cache.clear();
				LOGGER.debug("Cache {} has just been cleared", cacheName);
			}
		}
	}

}
