package org.ppodgorsek.cache.invalidation.strategy;

import java.util.List;

import org.ppodgorsek.cache.invalidation.exception.InvalidationException;
import org.ppodgorsek.cache.invalidation.model.InvalidationEntry;

/**
 * Strategy that invalidates entries.
 * 
 * @author Paul Podgorsek
 */
public interface InvalidationStrategy {

	/**
	 * Indicates whether the strategy can handle an entry or not.
	 * 
	 * @param entry
	 *            The invalidation entry.
	 * @return <code>true</code> if the entry can be invalidated by the strategy, <code>false</code> otherwise.
	 */
	boolean canHandle(InvalidationEntry entry);

	/**
	 * Gets the elements related to an invalidation entry.
	 * 
	 * @param entry
	 *            The invalidation entry.
	 * @return The elements related to the provided entry, or an empty list if there are none.
	 */
	List<InvalidationEntry> getRelatedEntries(InvalidationEntry entry);

	/**
	 * Invalidates a cache entry.
	 * 
	 * @param entry
	 *            The entry that must be invalidated.
	 * @throws InvalidationException
	 *             An exception thrown if the invalidation couldn't be performed.
	 */
	void invalidateEntry(InvalidationEntry entry) throws InvalidationException;

}
