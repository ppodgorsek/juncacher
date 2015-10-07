package org.ppodgorsek.cache.spring.strategy;

import org.ppodgorsek.cache.invalidation.exception.InvalidationException;
import org.ppodgorsek.cache.invalidation.model.InvalidationEntry;
import org.springframework.cache.CacheManager;

/**
 * Strategy used to determines which elements should be removed from the {@link CacheManager}.
 *
 * @since 1.0
 * @author Paul Podgorsek
 */
public interface CacheManagerStrategy<T extends InvalidationEntry> {

	/**
	 * Clears the cache associated with an invalidation entry.
	 *
	 * @param entry
	 *            The entry that must be invalidated.
	 */
	void invalidate(T entry) throws InvalidationException;

}
