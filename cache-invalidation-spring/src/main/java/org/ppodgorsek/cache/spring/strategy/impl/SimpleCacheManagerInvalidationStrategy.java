package org.ppodgorsek.cache.spring.strategy.impl;

import org.ppodgorsek.cache.invalidation.exception.InvalidationException;
import org.ppodgorsek.cache.invalidation.model.InvalidationEntry;
import org.ppodgorsek.cache.spring.strategy.AbstractCacheManagerInvalidationStrategy;
import org.ppodgorsek.cache.spring.strategy.CacheManagerStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;

/**
 * @author Paul Podgorsek
 */
public class SimpleCacheManagerInvalidationStrategy extends AbstractCacheManagerInvalidationStrategy<InvalidationEntry>
		implements CacheManagerStrategy<InvalidationEntry> {

	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleCacheManagerInvalidationStrategy.class);

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
