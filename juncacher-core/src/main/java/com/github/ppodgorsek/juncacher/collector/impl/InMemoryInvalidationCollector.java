package com.github.ppodgorsek.juncacher.collector.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.ppodgorsek.juncacher.collector.InvalidationCollector;
import com.github.ppodgorsek.juncacher.model.InvalidationEntry;

/**
 * An implementation of the invalidation collector that adds entries to a list in memory.
 *
 * @since 1.1
 * @author Paul Podgorsek
 */
public class InMemoryInvalidationCollector implements InvalidationCollector {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(InMemoryInvalidationCollector.class);

	private final List<InvalidationEntry> invalidationEntries = Collections
			.synchronizedList(new ArrayList<InvalidationEntry>());

	@Override
	public void addInvalidationEntries(final Collection<InvalidationEntry> entries) {

		for (final InvalidationEntry entry : entries) {
			addInvalidationEntry(entry);
		}
	}

	@Override
	public void addInvalidationEntry(final InvalidationEntry entry) {

		LOGGER.debug("Adding an invalidation entry: {}", entry);

		if (entry != null && !invalidationEntries.contains(entry)) {
			invalidationEntries.add(entry);
		}
	}

	@Override
	public void consume(final InvalidationEntry entry) {
		invalidationEntries.remove(entry);
	}

	@Override
	public void consume(final Collection<InvalidationEntry> entries) {
		invalidationEntries.removeAll(entries);
	}

	@Override
	public Collection<InvalidationEntry> getEntries() {
		return new ArrayList<>(invalidationEntries);
	}

}
