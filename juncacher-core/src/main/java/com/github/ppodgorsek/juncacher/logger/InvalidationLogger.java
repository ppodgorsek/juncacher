package com.github.ppodgorsek.juncacher.logger;

import java.util.Collection;
import java.util.List;

import com.github.ppodgorsek.juncacher.logger.impl.InMemoryInvalidationLogger;
import com.github.ppodgorsek.juncacher.model.InvalidationEntry;

/**
 * Logs invalidation entries and processes them. Most use cases will simply use an in-memory logger
 * ({@link InMemoryInvalidationLogger}) but other implementations could for example persist them to
 * a database in order to have only one instance of a cluster performing invalidations.
 *
 * @since 1.0
 * @author Paul Podgorsek
 */
public interface InvalidationLogger<T extends InvalidationEntry> {

	/**
	 * Add invalidation entries to the queue of elements to process.
	 *
	 * @param entries
	 *            The entries that must be added to the queue.
	 */
	void addInvalidationEntries(Collection<T> entries);

	/**
	 * Add an invalidation entry to the queue of elements to process.
	 *
	 * @param entry
	 *            The entry that must be added to the queue.
	 */
	void addInvalidationEntry(T entry);

	/**
	 * Consumes an invalidation entry. Once an entry has been consumed, it won't be in the logger
	 * anymore.
	 *
	 * @param entry
	 *            The entry to consume.
	 */
	void consume(T entry);

	/**
	 * Consumes invalidation entries. Once the entries have been consumed, they won't be in the
	 * logger anymore.
	 *
	 * @param entries
	 *            The entries to consume.
	 */
	void consume(List<T> entries);

	/**
	 * Return the list of invalidation entries that have already been added.
	 *
	 * @return The list of entries that were already added, or an empty list if there are none.
	 */
	List<T> getEntries();

}
