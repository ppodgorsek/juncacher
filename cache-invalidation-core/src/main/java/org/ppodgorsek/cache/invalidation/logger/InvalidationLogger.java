package org.ppodgorsek.cache.invalidation.logger;

import java.util.Collection;

import org.ppodgorsek.cache.invalidation.model.InvalidationEntry;

/**
 * Logs invalidation entries and processes them.
 * 
 * @author Paul Podgorsek
 */
public interface InvalidationLogger {

	/**
	 * Adds invalidation entries to the queue of elements to process.
	 * 
	 * @param entries
	 *            The entries that must be added to the queue.
	 */
	void addInvalidationEntries(Collection<InvalidationEntry> entries);

	/**
	 * Adds an invalidation entry to the queue of elements to process.
	 * 
	 * @param entry
	 *            The entry that must be added to the queue.
	 */
	void addInvalidationEntry(InvalidationEntry entry);

	/**
	 * Processes the invalidation entries which already exist.
	 */
	void processInvalidationEntries();

	/**
	 * Triggers the invalidation of an element without adding it to the queue. This is mandatory for elements that must be instantly invalidated.
	 * 
	 * @param entry
	 *            The entry that must be invalidated.
	 */
	void triggerDirectInvalidation(InvalidationEntry entry);

}
