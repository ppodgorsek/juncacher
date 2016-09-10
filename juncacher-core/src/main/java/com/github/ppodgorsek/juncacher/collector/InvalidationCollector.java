package com.github.ppodgorsek.juncacher.collector;

import java.util.Collection;
import java.util.List;

import com.github.ppodgorsek.juncacher.collector.impl.InMemoryInvalidationCollector;
import com.github.ppodgorsek.juncacher.model.InvalidationEntry;

/**
 * Collects invalidation entries them. Most use cases will simply use an in-memory collection
 * ({@link InMemoryInvalidationCollector}) but other implementations could for example persist them
 * to a database in order to have only one instance of a cluster performing invalidations.
 *
 * @since 1.1
 * @author Paul Podgorsek
 */
public interface InvalidationCollector {

	/**
	 * Add invalidation entries to the queue of elements to process.
	 *
	 * @param entries
	 *            The entries that must be added to the queue.
	 */
	void addInvalidationEntries(Collection<InvalidationEntry> entries);

	/**
	 * Add an invalidation entry to the queue of elements to process.
	 *
	 * @param entry
	 *            The entry that must be added to the queue.
	 */
	void addInvalidationEntry(InvalidationEntry entry);

	/**
	 * Consumes an invalidation entry. Once an entry has been consumed, it won't be in the logger
	 * anymore.
	 *
	 * @param entry
	 *            The entry to consume.
	 */
	void consume(InvalidationEntry entry);

	/**
	 * Consumes invalidation entries. Once the entries have been consumed, they won't be in the
	 * logger anymore.
	 *
	 * @param entries
	 *            The entries to consume.
	 */
	void consume(List<InvalidationEntry> entries);

	/**
	 * Return the list of invalidation entries that have already been added.
	 *
	 * @return The list of entries that were already added, or an empty list if there are none.
	 */
	List<InvalidationEntry> getEntries();

}
