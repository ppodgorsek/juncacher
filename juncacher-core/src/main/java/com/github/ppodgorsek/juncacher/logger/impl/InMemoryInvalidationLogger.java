package com.github.ppodgorsek.juncacher.logger.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.ppodgorsek.juncacher.logger.InvalidationLogger;
import com.github.ppodgorsek.juncacher.model.InvalidationEntry;

/**
 * An implementation of the invalidation logger that adds entries to a list in memory.
 *
 * @since 1.0
 * @author Paul Podgorsek
 */
public class InMemoryInvalidationLogger<T extends InvalidationEntry> implements
		InvalidationLogger<T> {

	private static final Logger LOGGER = LoggerFactory.getLogger(InMemoryInvalidationLogger.class);

	private final List<T> invalidationEntries = Collections.synchronizedList(new ArrayList<T>());

	@Override
	public void addInvalidationEntries(final Collection<T> entries) {

		for (final T entry : entries) {
			addInvalidationEntry(entry);
		}
	}

	@Override
	public void addInvalidationEntry(final T entry) {

		LOGGER.debug("Adding an invalidation entry: {}", entry);

		if (entry != null && !invalidationEntries.contains(entry)) {
			invalidationEntries.add(entry);
		}
	}

	@Override
	public void consume(final T entry) {
		invalidationEntries.remove(entry);
	}

	@Override
	public void consume(final List<T> entries) {
		invalidationEntries.removeAll(entries);
	}

	@Override
	public List<T> getEntries() {
		return new ArrayList<>(invalidationEntries);
	}

}
