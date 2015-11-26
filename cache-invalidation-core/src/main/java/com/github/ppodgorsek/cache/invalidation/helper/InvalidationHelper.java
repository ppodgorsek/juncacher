package com.github.ppodgorsek.cache.invalidation.helper;

import java.util.Collection;

import com.github.ppodgorsek.cache.invalidation.model.InvalidationEntry;

/**
 * Helper that invalidates entries.
 *
 * @since 1.0
 * @author Paul Podgorsek
 */
public interface InvalidationHelper<T extends InvalidationEntry> {

	/**
	 * Invalidate cache entries.
	 *
	 * @param entries
	 *            The entries that must be invalidated.
	 */
	void invalidate(Collection<T> entries);

}
