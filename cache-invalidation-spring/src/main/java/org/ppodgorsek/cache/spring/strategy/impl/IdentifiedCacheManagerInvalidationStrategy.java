package org.ppodgorsek.cache.spring.strategy.impl;

import org.ppodgorsek.cache.invalidation.exception.InvalidationException;
import org.ppodgorsek.cache.invalidation.model.impl.IdentifiedInvalidationEntry;
import org.ppodgorsek.cache.spring.strategy.AbstractCacheManagerInvalidationStrategy;
import org.ppodgorsek.cache.spring.strategy.CacheManagerStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.util.Assert;

/**
 * Strategy that evicts a single element from cache regions when an entry needs to be invalidated.
 *
 * @since 1.0
 * @author Paul Podgorsek
 */
public class IdentifiedCacheManagerInvalidationStrategy
		extends AbstractCacheManagerInvalidationStrategy<IdentifiedInvalidationEntry>
		implements CacheManagerStrategy<IdentifiedInvalidationEntry> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(IdentifiedCacheManagerInvalidationStrategy.class);

	@Override
	public void invalidate(final IdentifiedInvalidationEntry entry) throws InvalidationException {

		Assert.notNull(entry, "The entry is required");

		final String entryId = entry.getId();

		if (entryId == null) {
			LOGGER.warn("Invalidation entry {} has no ID, can't evict it from the cache", entry);
		}
		else {
			for (final String cacheName : getCacheNames()) {

				final Cache cache = getCacheManager().getCache(cacheName);

				if (cache != null) {
					cache.evict(entryId);
					LOGGER.debug("Evicted entry '{}' from cache {}", entryId, cacheName);
				}
			}
		}
	}

}
