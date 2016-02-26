package com.github.ppodgorsek.juncacher.logger.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.ppodgorsek.juncacher.logger.InvalidationLogger;
import com.github.ppodgorsek.juncacher.model.InvalidationEntry;

/**
 * An invalidation logger that does nothing: no adding, no consuming, always returns empty lists of
 * entries.
 *
 * @since 1.0
 * @author Paul Podgorsek
 */
public class NoOpInvalidationLogger<T extends InvalidationEntry> implements InvalidationLogger<T> {

	private static final Logger LOGGER = LoggerFactory.getLogger(NoOpInvalidationLogger.class);

	@Override
	public void addInvalidationEntries(final Collection<T> entries) {
		LOGGER.trace("Adding invalidation entries: {}", entries);
	}

	@Override
	public void addInvalidationEntry(final T entry) {
		LOGGER.trace("Adding an invalidation entry: {}", entry);
	}

	@Override
	public void consume(final T entry) {
		LOGGER.trace("Consuming an invalidation entry: {}", entry);
	}

	@Override
	public void consume(final List<T> entries) {
		LOGGER.trace("Consuming invalidation entries: {}", entries);
	}

	@Override
	public List<T> getEntries() {

		LOGGER.debug("Returning an empty list of elements");

		return Collections.emptyList();
	}

}
