package org.ppodgorsek.cache.spring.strategy;

import org.ppodgorsek.cache.invalidation.exception.InvalidationException;
import org.ppodgorsek.cache.invalidation.model.InvalidationEntry;

/**
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
