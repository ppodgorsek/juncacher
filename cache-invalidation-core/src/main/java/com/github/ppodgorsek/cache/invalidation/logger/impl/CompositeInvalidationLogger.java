package com.github.ppodgorsek.cache.invalidation.logger.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import com.github.ppodgorsek.cache.invalidation.logger.InvalidationLogger;
import com.github.ppodgorsek.cache.invalidation.model.InvalidationEntry;

/**
 * Invalidation logger that reads entries from several delegate loggers and adds entries either to
 * all delegate loggers or only to the first one of them (no need to process them several times).
 *
 * @since 1.0
 * @author Paul Podgorsek
 */
public class CompositeInvalidationLogger<T extends InvalidationEntry> implements
		InvalidationLogger<T> {

	private List<InvalidationLogger<T>> invalidationLoggers;

	private boolean addToAllDelegates;

	@Override
	public void addInvalidationEntries(final Collection<T> entries) {

		for (final InvalidationLogger<T> invalidationLogger : getInvalidationLoggers()) {
			invalidationLogger.addInvalidationEntries(entries);

			if (!isAddToAllDelegates()) {
				break;
			}
		}
	}

	@Override
	public void addInvalidationEntry(final T entry) {

		for (final InvalidationLogger<T> invalidationLogger : getInvalidationLoggers()) {
			invalidationLogger.addInvalidationEntry(entry);

			if (!isAddToAllDelegates()) {
				break;
			}
		}
	}

	@Override
	public void consume(final T entry) {

		for (final InvalidationLogger<T> invalidationLogger : getInvalidationLoggers()) {
			invalidationLogger.consume(entry);
		}
	}

	@Override
	public void consume(final List<T> entries) {

		for (final InvalidationLogger<T> invalidationLogger : getInvalidationLoggers()) {
			invalidationLogger.consume(entries);
		}
	}

	@Override
	public List<T> getEntries() {

		final List<T> entries = new ArrayList<>();

		for (final InvalidationLogger<T> invalidationLogger : getInvalidationLoggers()) {
			entries.addAll(invalidationLogger.getEntries());
		}

		return entries;
	}

	public List<InvalidationLogger<T>> getInvalidationLoggers() {
		return invalidationLoggers;
	}

	@Required
	public void setInvalidationLoggers(final List<InvalidationLogger<T>> loggers) {
		invalidationLoggers = loggers;
	}

	public boolean isAddToAllDelegates() {
		return addToAllDelegates;
	}

	public void setAddToAllDelegates(final boolean addToAll) {
		addToAllDelegates = addToAll;
	}

}
