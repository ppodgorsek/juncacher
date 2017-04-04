package com.github.ppodgorsek.juncacher.manager.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.github.ppodgorsek.juncacher.collector.InvalidationCollector;
import com.github.ppodgorsek.juncacher.collector.impl.InMemoryInvalidationCollector;
import com.github.ppodgorsek.juncacher.manager.InvalidationManager;
import com.github.ppodgorsek.juncacher.model.InvalidationEntry;
import com.github.ppodgorsek.juncacher.processor.InvalidationProcessor;

/**
 * A manager that coordinates the invalidation from a list of processors.
 *
 * @since 1.1
 * @author Paul Podgorsek
 */
public class ProcessorInvalidationManager implements InvalidationManager {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ProcessorInvalidationManager.class);

	private final Map<InvalidationProcessor, InvalidationCollector> processorsWithCollectors;

	/**
	 * The collector of the first processor defined. This attribute is defined separately to improve
	 * the performance while adding new invalidation entries.
	 */
	private final InvalidationCollector firstCollector;

	/**
	 * Default constructor allowing to set the processors along with their respective collectors.
	 *
	 * @param processorsWithCollectors
	 *            The processors with their collectors, none of them can be {@code null}.
	 */
	public ProcessorInvalidationManager(
			final Map<InvalidationProcessor, InvalidationCollector> processorsWithCollectors) {

		super();

		Assert.isTrue(!CollectionUtils.isEmpty(processorsWithCollectors),
				"The invalidation manager must have processors");

		final Map<InvalidationProcessor, InvalidationCollector> checkedProcessorsWithCollectors = new LinkedHashMap<>();

		for (final Entry<InvalidationProcessor, InvalidationCollector> processorWithCollector : processorsWithCollectors
				.entrySet()) {

			final InvalidationProcessor processor = processorWithCollector.getKey();
			final InvalidationCollector collector = processorWithCollector.getValue();

			Assert.notNull(processor, "The processor can't be null");
			Assert.notNull(collector, "The collector can't be null");

			checkedProcessorsWithCollectors.put(processor, collector);
		}

		this.processorsWithCollectors = Collections
				.unmodifiableMap(checkedProcessorsWithCollectors);
		firstCollector = checkedProcessorsWithCollectors.entrySet().iterator().next().getValue();
	}

	@Override
	public void addInvalidationEntries(final Collection<InvalidationEntry> entries) {
		firstCollector.addInvalidationEntries(entries);
	}

	@Override
	public void addInvalidationEntry(final InvalidationEntry entry) {
		firstCollector.addInvalidationEntry(entry);
	}

	@Override
	public void processEntries() {

		LOGGER.debug("Processing the invalidation entries");

		Collection<InvalidationEntry> entriesForNextProcessor = new ArrayList<>();

		for (final Entry<InvalidationProcessor, InvalidationCollector> processorWithCollector : processorsWithCollectors
				.entrySet()) {

			final InvalidationProcessor processor = processorWithCollector.getKey();
			final InvalidationCollector collector = processorWithCollector.getValue();
			collector.addInvalidationEntries(entriesForNextProcessor);

			entriesForNextProcessor = processor.invalidateEntries(collector.getEntries());

			collector.consume(entriesForNextProcessor);
		}
	}

	@Override
	public void processEntries(final Collection<InvalidationEntry> entries) {

		LOGGER.debug("Immediate processing of invalidation entries: {}", entries);

		Collection<InvalidationEntry> entriesForNextProcessor = entries;

		for (final Entry<InvalidationProcessor, InvalidationCollector> processorWithCollector : processorsWithCollectors
				.entrySet()) {

			final InvalidationProcessor processor = processorWithCollector.getKey();
			final InvalidationCollector collector = processorWithCollector.getValue();
			final InvalidationCollector temporaryCollector = new InMemoryInvalidationCollector();

			temporaryCollector.addInvalidationEntries(entriesForNextProcessor);
			entriesForNextProcessor = processor.invalidateEntries(entriesForNextProcessor);
			temporaryCollector.consume(entriesForNextProcessor);

			final Collection<InvalidationEntry> remainingEntries = temporaryCollector.getEntries();

			if (!CollectionUtils.isEmpty(remainingEntries)) {
				LOGGER.warn("Several entries were not immediately invalidated by {}: {}", processor,
						remainingEntries);
				collector.addInvalidationEntries(remainingEntries);
			}
		}
	}

	@Override
	public void processEntry(final InvalidationEntry entry) {
		processEntries(Collections.singletonList(entry));
	}

	public Map<InvalidationProcessor, InvalidationCollector> getProcessorsWithCollectors() {
		return processorsWithCollectors;
	}

}
