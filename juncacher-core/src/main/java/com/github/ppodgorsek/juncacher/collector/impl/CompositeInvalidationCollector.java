package com.github.ppodgorsek.juncacher.collector.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import com.github.ppodgorsek.juncacher.collector.InvalidationCollector;
import com.github.ppodgorsek.juncacher.model.InvalidationEntry;

/**
 * Composite invalidation collector that has one collector to read / add entries, another to consume
 * entries. This is useful when having a chain of invalidation processors if entries must only be
 * consumed when all processors have processed them.
 *
 * @since 1.1
 * @author Paul Podgorsek
 */
public class CompositeInvalidationCollector implements InvalidationCollector {

	private InvalidationCollector reader;

	private InvalidationCollector consumer;

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

	public InvalidationCollector getReader() {
		return reader;
	}

	@Required
	public void setReader(final InvalidationCollector entryReader) {
		reader = entryReader;
	}

	public InvalidationCollector getConsumer() {
		return consumer;
	}

	@Required
	public void setConsumer(final InvalidationCollector entryConsumer) {
		consumer = entryConsumer;
	}

}
