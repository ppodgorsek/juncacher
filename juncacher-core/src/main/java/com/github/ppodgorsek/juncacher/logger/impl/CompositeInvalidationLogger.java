package com.github.ppodgorsek.juncacher.logger.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import com.github.ppodgorsek.juncacher.logger.InvalidationLogger;
import com.github.ppodgorsek.juncacher.model.InvalidationEntry;

/**
 * Composite invalidation logger that has one logger to read / add entries, another logger to
 * consume entries. This is useful when having a chain of invalidation helpers if entries must only
 * be consumed when all helpers have processed them.
 *
 * @since 1.0
 * @author Paul Podgorsek
 */
public class CompositeInvalidationLogger implements InvalidationLogger {

	private InvalidationLogger reader;

	private InvalidationLogger consumer;

	@Override
	public void addInvalidationEntries(final Collection<InvalidationEntry> entries) {
		reader.addInvalidationEntries(entries);
	}

	@Override
	public void addInvalidationEntry(final InvalidationEntry entry) {
		reader.addInvalidationEntry(entry);
	}

	@Override
	public void consume(final InvalidationEntry entry) {
		consumer.consume(entry);
	}

	@Override
	public void consume(final List<InvalidationEntry> entries) {
		consumer.consume(entries);
	}

	@Override
	public List<InvalidationEntry> getEntries() {
		return reader.getEntries();
	}

	public InvalidationLogger getReader() {
		return reader;
	}

	@Required
	public void setReader(final InvalidationLogger entryReader) {
		reader = entryReader;
	}

	public InvalidationLogger getConsumer() {
		return consumer;
	}

	@Required
	public void setConsumer(final InvalidationLogger entryConsumer) {
		consumer = entryConsumer;
	}

}
