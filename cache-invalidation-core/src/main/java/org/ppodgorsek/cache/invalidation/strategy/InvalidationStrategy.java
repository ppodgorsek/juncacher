package org.ppodgorsek.cache.invalidation.strategy;

import org.ppodgorsek.cache.invalidation.exception.InvalidationException;
import org.ppodgorsek.cache.invalidation.model.InvalidationEntry;

/**
 * Strategy that invalidates entries.
 * 
 * @author Paul Podgorsek
 */
public interface InvalidationStrategy<T extends InvalidationEntry> {

	/**
	 * Indicates whether the strategy can handle an entry or not.
	 * 
	 * @param entry
	 *            The invalidation entry.
	 * @return <code>true</code> if the entry can be invalidated by the strategy, <code>false</code> otherwise.
	 */
	boolean canHandle(InvalidationEntry entry);

	/**
	 * Delegates the invalidation of an entry to one or more helpers.
	 * 
	 * @param entry
	 *            The entry that must be invalidated.
	 * @throws InvalidationException
	 *             An exception thrown if the invalidation couldn't be performed.
	 */
	void delegateInvalidation(T entry) throws InvalidationException;

}
