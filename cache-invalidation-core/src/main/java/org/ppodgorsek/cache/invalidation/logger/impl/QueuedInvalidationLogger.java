package org.ppodgorsek.cache.invalidation.logger.impl;

import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.ppodgorsek.cache.invalidation.exception.InvalidationException;
import org.ppodgorsek.cache.invalidation.logger.InvalidationLogger;
import org.ppodgorsek.cache.invalidation.model.InvalidationEntry;
import org.ppodgorsek.cache.invalidation.strategy.InvalidationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * An implementation of the invalidation logger that adds entries to a queue.
 * 
 * @author Paul Podgorsek
 */
public class QueuedInvalidationLogger implements InvalidationLogger {

	private static final Logger LOGGER = LoggerFactory.getLogger(QueuedInvalidationLogger.class);

	private final Queue<InvalidationEntry> queue = new ConcurrentLinkedQueue<InvalidationEntry>();

	private List<InvalidationStrategy<InvalidationEntry>> strategies;

	/**
	 * Default constructor.
	 */
	public QueuedInvalidationLogger() {
		super();
	}

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
	public void processInvalidationEntries() {

		LOGGER.debug("Processing the invalidation entries");

		InvalidationEntry entry = queue.poll();

		while (entry != null) {

			processInvalidationEntry(entry);
			entry = queue.poll();
		}
	}

	@Override
	public void triggerDirectInvalidation(final InvalidationEntry entry) {

		LOGGER.debug("Triggering a direct invalidation: {}", entry);

		processInvalidationEntry(entry);
	}

	/**
	 * Processes an invalidation entry by using the appropriate invalidation strategy.
	 * 
	 * @param entry
	 *            The invalidation entry that must be processed.
	 */
	private void processInvalidationEntry(final InvalidationEntry entry) {

		LOGGER.debug("Processing an invalidation entry: {}", entry);

		for (InvalidationStrategy<InvalidationEntry> strategy : strategies) {

			if (strategy.canHandle(entry)) {

				try {
					strategy.delegateInvalidation(entry);
				}
				catch (final InvalidationException e) {
					LOGGER.warn("{} can't be invalidated, it will be replaced on the queue", entry);
					addInvalidationEntry(entry);
				}
			}
		}
	}

	@Required
	public void setStrategies(final List<InvalidationStrategy<InvalidationEntry>> newStrategies) {
		strategies = newStrategies;
	}

}
