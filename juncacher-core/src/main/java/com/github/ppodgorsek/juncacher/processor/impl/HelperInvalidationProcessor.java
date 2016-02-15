package com.github.ppodgorsek.juncacher.processor.impl;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import com.github.ppodgorsek.juncacher.helper.InvalidationHelper;
import com.github.ppodgorsek.juncacher.logger.InvalidationLogger;
import com.github.ppodgorsek.juncacher.model.InvalidationEntry;
import com.github.ppodgorsek.juncacher.processor.InvalidationProcessor;

/**
 * An invalidation processor that triggers the invalidation from a helper.
 *
 * @since 1.0
 * @author Paul Podgorsek
 */
public class HelperInvalidationProcessor implements InvalidationProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(HelperInvalidationProcessor.class);

	private InvalidationHelper<InvalidationEntry> invalidationHelper;

	@Override
	public void addInvalidationEntries(final Collection<InvalidationEntry> entries) {

		if (invalidationHelper == null) {
			LOGGER.warn("The invalidation processor has no helper, impossible to add entries");
		}
		else {
			final InvalidationLogger<InvalidationEntry> logger = invalidationHelper.getLogger();

			if (logger == null) {
				LOGGER.warn("The invalidation helper has no logger, impossible to add entries");
			}
			else {
				logger.addInvalidationEntries(entries);
			}
		}
	}

	@Override
	public void addInvalidationEntry(final InvalidationEntry entry) {

		if (invalidationHelper == null) {
			LOGGER.warn("The invalidation processor has no helper, impossible to add entry");
		}
		else {
			final InvalidationLogger<InvalidationEntry> logger = invalidationHelper.getLogger();

			if (logger == null) {
				LOGGER.warn("The invalidation helper has no logger, impossible to add entry");
			}
			else {
				logger.addInvalidationEntry(entry);
			}
		}
	}

	@Override
	public void processEntries() {

		LOGGER.debug("Processing the invalidation entries");

		if (invalidationHelper == null) {
			LOGGER.warn("The invalidation processor has no helper, impossible to process entries");
		}
		else {
			invalidationHelper.invalidateEntries();
		}
	}

	protected InvalidationHelper<InvalidationEntry> getInvalidationHelper() {
		return invalidationHelper;
	}

	@Required
	public void setInvalidationHelper(final InvalidationHelper<InvalidationEntry> helper) {
		invalidationHelper = helper;
	}

}
