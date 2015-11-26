package com.github.ppodgorsek.cache.spring.strategy;

import org.springframework.cache.CacheManager;

import com.github.ppodgorsek.cache.invalidation.exception.InvalidationException;
import com.github.ppodgorsek.cache.invalidation.model.InvalidationEntry;

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
