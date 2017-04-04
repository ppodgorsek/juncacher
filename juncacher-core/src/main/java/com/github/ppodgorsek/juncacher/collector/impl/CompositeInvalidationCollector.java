package com.github.ppodgorsek.juncacher.collector.impl;

import java.util.Collection;

import org.springframework.util.Assert;

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

	/**
	 * Default constructor that allows to set the reader and consumer collectors.
	 *
	 * @param reader
	 *            The collector from which entries will be read.
	 * @param consumer
	 *            The collector in which entries will be put.
	 */
	public CompositeInvalidationCollector(final InvalidationCollector reader,
			final InvalidationCollector consumer) {

		super();

		Assert.notNull(reader, "The reader is required");
		Assert.notNull(consumer, "The consumer is required");

		this.reader = reader;
		this.consumer = consumer;
	}

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
	public void consume(final Collection<InvalidationEntry> entries) {
		consumer.consume(entries);
	}

	@Override
	public Collection<InvalidationEntry> getEntries() {
		return reader.getEntries();
	}

	public InvalidationCollector getReader() {
		return reader;
	}

	public void setReader(final InvalidationCollector entryReader) {

		Assert.notNull(entryReader, "The reader is required");

		reader = entryReader;
	}

	public InvalidationCollector getConsumer() {
		return consumer;
	}

	public void setConsumer(final InvalidationCollector entryConsumer) {

		Assert.notNull(entryConsumer, "The consumer is required");

		consumer = entryConsumer;
	}

}
