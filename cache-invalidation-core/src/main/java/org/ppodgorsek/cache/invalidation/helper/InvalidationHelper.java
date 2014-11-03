package org.ppodgorsek.cache.invalidation.helper;

import org.ppodgorsek.cache.invalidation.exception.InvalidationException;
import org.ppodgorsek.cache.invalidation.model.InvalidationEntry;

/**
 * Helper that invalidates entries.
 * 
 * @author Paul Podgorsek
 */
public interface InvalidationHelper<T extends InvalidationEntry> {

	/**
	 * Invalidates a cache entry.
	 * 
	 * @param entry
	 *            The entry that must be invalidated.
	 * @throws InvalidationException
	 *             An exception thrown if the invalidation couldn't be performed.
	 */
	void invalidateEntry(T entry) throws InvalidationException;

}
