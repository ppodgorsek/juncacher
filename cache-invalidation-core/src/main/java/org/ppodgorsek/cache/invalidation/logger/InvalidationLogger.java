package org.ppodgorsek.cache.invalidation.logger;

import java.util.Collection;
import java.util.List;

import org.ppodgorsek.cache.invalidation.logger.impl.InMemoryInvalidationLogger;
import org.ppodgorsek.cache.invalidation.model.InvalidationEntry;

/**
 * Logs invalidation entries and processes them. Most use cases will simply use an in-memory logger
 * ({@link InMemoryInvalidationLogger}) but other implementations could for example persist them to
 * a database in order to have only one instance of a cluster performing invalidations.
 *
 * @since 1.0
 * @author Paul Podgorsek
 */
public interface InvalidationLogger {

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
	 * Return the list of invalidation entries that have already been added.
	 *
	 * @return The list of entries that were already added, or an empty list if there are none.
	 */
	List<InvalidationEntry> getEntries();

}
