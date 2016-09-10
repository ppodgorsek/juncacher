package com.github.ppodgorsek.juncacher.logger.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import com.github.ppodgorsek.juncacher.logger.InvalidationLogger;
import com.github.ppodgorsek.juncacher.model.InvalidationEntry;

/**
 * Invalidation logger that reads entries from several delegate loggers and adds entries either to
 * all delegate loggers or only to the first one of them (no need to process them several times).
 *
 * @since 1.0
 * @author Paul Podgorsek
 */
public class DelegateInvalidationLogger implements InvalidationLogger {

	private List<InvalidationLogger> invalidationLoggers;

	private boolean addToAllDelegates;

	@Override
	public void addInvalidationEntries(final Collection<InvalidationEntry> entries) {

		for (final InvalidationLogger invalidationLogger : getInvalidationLoggers()) {
			invalidationLogger.addInvalidationEntries(entries);

			if (!isAddToAllDelegates()) {
				break;
			}
		}
	}

	@Override
	public void addInvalidationEntry(final InvalidationEntry entry) {

		for (final InvalidationLogger invalidationLogger : getInvalidationLoggers()) {
			invalidationLogger.addInvalidationEntry(entry);

			if (!isAddToAllDelegates()) {
				break;
			}
		}
	}

	@Override
	public void consume(final InvalidationEntry entry) {

		for (final InvalidationLogger invalidationLogger : getInvalidationLoggers()) {
			invalidationLogger.consume(entry);
		}
	}

	@Override
	public void consume(final List<InvalidationEntry> entries) {

		for (final InvalidationLogger invalidationLogger : getInvalidationLoggers()) {
			invalidationLogger.consume(entries);
		}
	}

	@Override
	public List<InvalidationEntry> getEntries() {

		final List<InvalidationEntry> entries = new ArrayList<>();

		for (final InvalidationLogger invalidationLogger : getInvalidationLoggers()) {
			entries.addAll(invalidationLogger.getEntries());
		}

		return entries;
	}

	public List<InvalidationLogger> getInvalidationLoggers() {
		return invalidationLoggers;
	}

	@Required
	public void setInvalidationLoggers(final List<InvalidationLogger> loggers) {
		invalidationLoggers = loggers;
	}

	public boolean isAddToAllDelegates() {
		return addToAllDelegates;
	}

	public void setAddToAllDelegates(final boolean addToAll) {
		addToAllDelegates = addToAll;
	}

}
