package org.ppodgorsek.cache.invalidation.logger.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.ppodgorsek.cache.invalidation.logger.InvalidationLogger;
import org.ppodgorsek.cache.invalidation.model.InvalidationEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An implementation of the invalidation logger that adds entries to a queue in memory.
 *
 * @since 1.0
 * @author Paul Podgorsek
 */
public class InMemoryInvalidationLogger implements InvalidationLogger {

	private static final Logger LOGGER = LoggerFactory.getLogger(InMemoryInvalidationLogger.class);

	private final Queue<InvalidationEntry> queue = new ConcurrentLinkedQueue<InvalidationEntry>();

	@Override
	public void addInvalidationEntries(final Collection<InvalidationEntry> entries) {

		for (final InvalidationEntry entry : entries) {
			addInvalidationEntry(entry);
		}
	}

	@Override
	public void addInvalidationEntry(final InvalidationEntry entry) {

		LOGGER.debug("Adding an invalidation entry: {}", entry);

		if (entry != null && !queue.contains(entry)) {
			queue.offer(entry);
		}
	}

	@Override
	public List<InvalidationEntry> getEntries() {

		final List<InvalidationEntry> entries = new ArrayList<>();

		while (!queue.isEmpty()) {
			entries.add(queue.poll());
		}

		return entries;
	}

}
