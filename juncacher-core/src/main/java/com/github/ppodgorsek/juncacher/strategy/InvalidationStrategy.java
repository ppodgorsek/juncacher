package com.github.ppodgorsek.juncacher.strategy;

import com.github.ppodgorsek.juncacher.exception.InvalidationException;
import com.github.ppodgorsek.juncacher.model.InvalidationEntry;

/**
 * Marker interface for strategies of invalidation used by helpers.
 *
 * @since 1.0
 * @author Paul Podgorsek
 */
public interface InvalidationStrategy<T extends InvalidationEntry> {

	/**
	 * Checks whether the strategy can handle an invalidation entry or not.
	 *
	 * @param entry
	 *            The invalidation entry.
	 * @return {@code true} if the strategy can handle the provided invalidation entry,
	 *         {@code false} otherwise.
	 */
	boolean canHandle(InvalidationEntry entry);

	/**
	 * Invalidates a cache entry.
	 *
	 * @param entry
	 *            The entry that must be invalidated.
	 * @throws InvalidationException
	 *             An exception thrown if the invalidation couldn't be performed.
	 */
	void invalidate(T entry) throws InvalidationException;

}
