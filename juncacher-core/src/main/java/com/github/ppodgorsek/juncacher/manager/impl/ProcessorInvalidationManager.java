package com.github.ppodgorsek.juncacher.manager.impl;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import com.github.ppodgorsek.juncacher.collector.InvalidationCollector;
import com.github.ppodgorsek.juncacher.manager.InvalidationManager;
import com.github.ppodgorsek.juncacher.model.InvalidationEntry;
import com.github.ppodgorsek.juncacher.processor.InvalidationProcessor;

/**
 * An invalidation manager that triggers the invalidation from a processor.
 *
 * @since 1.1
 * @author Paul Podgorsek
 */
public class ProcessorInvalidationManager implements InvalidationManager {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ProcessorInvalidationManager.class);

	private InvalidationProcessor invalidationProcessor;

	@Override
	public void addInvalidationEntries(final Collection<InvalidationEntry> entries) {

		if (invalidationProcessor == null) {
			LOGGER.warn("The invalidation manager has no processor, impossible to add entries");
		}
		else {
			final InvalidationCollector collector = invalidationProcessor.getCollector();

			if (collector == null) {
				LOGGER.warn(
						"The invalidation processor has no collector, impossible to add entries");
			}
			else {
				collector.addInvalidationEntries(entries);
			}
		}
	}

	@Override
	public void addInvalidationEntry(final InvalidationEntry entry) {

		if (invalidationProcessor == null) {
			LOGGER.warn("The invalidation manager has no processor, impossible to add entry");
		}
		else {
			final InvalidationCollector collector = invalidationProcessor.getCollector();

			if (collector == null) {
				LOGGER.warn("The invalidation processor has no collector, impossible to add entry");
			}
			else {
				collector.addInvalidationEntry(entry);
			}
		}
	}

	@Override
	public void processEntries() {

		LOGGER.debug("Processing the invalidation entries");

		if (invalidationProcessor == null) {
			LOGGER.warn("The invalidation manager has no processor, impossible to process entries");
		}
		else {
			invalidationProcessor.invalidateEntries();
		}
	}

	protected InvalidationProcessor getInvalidationProcessor() {
		return invalidationProcessor;
	}

	@Required
	public void setInvalidationProcessor(final InvalidationProcessor processor) {
		invalidationProcessor = processor;
	}

}
