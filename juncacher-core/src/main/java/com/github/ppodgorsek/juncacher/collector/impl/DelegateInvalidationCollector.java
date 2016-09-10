package com.github.ppodgorsek.juncacher.collector.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import com.github.ppodgorsek.juncacher.collector.InvalidationCollector;
import com.github.ppodgorsek.juncacher.model.InvalidationEntry;

/**
 * Invalidation collector that reads entries from several delegate collectors and adds entries
 * either to all delegate collectors or only to the first one of them (no need to process them
 * several times).
 *
 * @since 1.1
 * @author Paul Podgorsek
 */
public class DelegateInvalidationCollector implements InvalidationCollector {

	private List<InvalidationCollector> invalidationCollectors;

	private boolean addToAllDelegates;

	@Override
	public void addInvalidationEntries(final Collection<InvalidationEntry> entries) {

		for (final InvalidationCollector invalidationCollector : getInvalidationCollectors()) {
			invalidationCollector.addInvalidationEntries(entries);

			if (!isAddToAllDelegates()) {
				break;
			}
		}
	}

	@Override
	public void addInvalidationEntry(final InvalidationEntry entry) {

		for (final InvalidationCollector invalidationCollector : getInvalidationCollectors()) {
			invalidationCollector.addInvalidationEntry(entry);

			if (!isAddToAllDelegates()) {
				break;
			}
		}
	}

	@Override
	public void consume(final InvalidationEntry entry) {

		for (final InvalidationCollector invalidationCollector : getInvalidationCollectors()) {
			invalidationCollector.consume(entry);
		}
	}

	@Override
	public void consume(final List<InvalidationEntry> entries) {

		for (final InvalidationCollector invalidationCollector : getInvalidationCollectors()) {
			invalidationCollector.consume(entries);
		}
	}

	@Override
	public List<InvalidationEntry> getEntries() {

		final List<InvalidationEntry> entries = new ArrayList<>();

		for (final InvalidationCollector invalidationCollector : getInvalidationCollectors()) {
			entries.addAll(invalidationCollector.getEntries());
		}

		return entries;
	}

	public List<InvalidationCollector> getInvalidationCollectors() {
		return invalidationCollectors;
	}

	@Required
	public void setInvalidationCollectors(final List<InvalidationCollector> collectors) {
		invalidationCollectors = collectors;
	}

	public boolean isAddToAllDelegates() {
		return addToAllDelegates;
	}

	public void setAddToAllDelegates(final boolean addToAll) {
		addToAllDelegates = addToAll;
	}

}
