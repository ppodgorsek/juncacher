package com.github.ppodgorsek.juncacher.spring.strategy.impl;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.util.Assert;

import com.github.ppodgorsek.juncacher.exception.InvalidationException;
import com.github.ppodgorsek.juncacher.model.InvalidationEntry;
import com.github.ppodgorsek.juncacher.model.impl.IdentifiedInvalidationEntry;
import com.github.ppodgorsek.juncacher.spring.strategy.AbstractCacheManagerInvalidationStrategy;
import com.github.ppodgorsek.juncacher.strategy.InvalidationStrategy;

/**
 * Strategy that evicts a single element from cache regions when an entry needs to be invalidated.
 *
 * @since 1.0
 * @author Paul Podgorsek
 */
public class IdentifiedCacheManagerInvalidationStrategy
		extends AbstractCacheManagerInvalidationStrategy<IdentifiedInvalidationEntry>
		implements InvalidationStrategy<IdentifiedInvalidationEntry> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(IdentifiedCacheManagerInvalidationStrategy.class);

	@Override
	public boolean canHandle(final InvalidationEntry entry) {
		return entry instanceof IdentifiedInvalidationEntry;
	}

	@Override
	public void invalidate(final IdentifiedInvalidationEntry entry) throws InvalidationException {

		Assert.notNull(entry, "The entry is required");

		final List<String> cacheKeys = getCacheKeys(entry);

		for (final String cacheName : getCacheNames()) {

			final Cache cache = getCacheManager().getCache(cacheName);

			if (cache != null) {
				for (final String cacheKey : cacheKeys) {
					cache.evict(cacheKey);
					LOGGER.debug("Evicted entry '{}' from cache {}", cacheKey, cacheName);
				}
			}
		}
	}

	/**
	 * Generates the list of cache keys related to an invalidation entry.
	 *
	 * @param entry
	 *            The invalidation entry.
	 * @return The list of cache keys for the provided invalidation entry, or an empty list if there
	 *         are none.
	 */
	public List<String> getCacheKeys(final IdentifiedInvalidationEntry entry) {

		final String entryId = entry.getReferenceId();

		if (entryId == null) {
			LOGGER.warn("Invalidation entry {} has no ID, can't evict it from the cache", entry);
			return Collections.emptyList();
		}
		else {
			return Collections.singletonList(entryId);
		}
	}

}
