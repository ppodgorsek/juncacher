package com.github.ppodgorsek.cache.invalidation.logger.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import com.github.ppodgorsek.cache.invalidation.logger.InvalidationLogger;
import com.github.ppodgorsek.cache.invalidation.model.InvalidationEntry;

/**
 * Composite invalidation logger that has one logger to read / add entries, another logger to
 * consume entries. This is useful when having a chain of invalidation helpers if entries must only
 * be consumed when all helpers have processed them.
 *
 * @since 1.0
 * @author Paul Podgorsek
 */
public class CompositeInvalidationLogger<T extends InvalidationEntry>
		implements InvalidationLogger<T> {

	private InvalidationLogger<T> reader;

	private InvalidationLogger<T> consumer;

	@Override
	public void addInvalidationEntries(final Collection<T> entries) {
		reader.addInvalidationEntries(entries);
	}

	@Override
	public void addInvalidationEntry(final T entry) {
		reader.addInvalidationEntry(entry);
	}

	@Override
	public void consume(final T entry) {
		consumer.consume(entry);
	}

	@Override
	public void consume(final List<T> entries) {
		consumer.consume(entries);
	}

	@Override
	public List<T> getEntries() {
		return reader.getEntries();
	}

	public InvalidationLogger<T> getReader() {
		return reader;
	}

	@Required
	public void setReader(final InvalidationLogger<T> entryReader) {
		reader = entryReader;
	}

	public InvalidationLogger<T> getConsumer() {
		return consumer;
	}

	@Required
	public void setConsumer(final InvalidationLogger<T> entryConsumer) {
		consumer = entryConsumer;
	}

}
