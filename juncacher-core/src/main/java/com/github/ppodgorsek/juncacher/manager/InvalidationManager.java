package com.github.ppodgorsek.juncacher.manager;

import java.util.Collection;

import com.github.ppodgorsek.juncacher.model.InvalidationEntry;

/**
 * Processes invalidation entries that were previously created. These entries could be for example
 * read from a collector or directly from a database.
 *
 * @since 1.1
 * @author Paul Podgorsek
 */
public interface InvalidationManager {

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
	 * Process the invalidation entries which already exist.
	 */
	void processEntries();

	/**
	 * Processes invalidation entries immediately.
	 *
	 * @param entries
	 *            The entries that must be invalidated immediately.
	 */
	void processEntries(Collection<InvalidationEntry> entries);

	/**
	 * Processes an invalidation entry immediately.
	 *
	 * @param entry
	 *            The entry that must be invalidated immediately.
	 */
	void processEntry(InvalidationEntry entry);

}
