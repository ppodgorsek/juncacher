package com.github.ppodgorsek.juncacher.processor;

import java.util.Collection;

import com.github.ppodgorsek.juncacher.model.InvalidationEntry;

/**
 * Processes invalidation entries that were previously created. These entries could be for example
 * read from a logger or directly from a database.
 *
 * @since 1.0
 * @author Paul Podgorsek
 */
public interface InvalidationProcessor {

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

}
