package com.github.ppodgorsek.juncacher.processor.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import com.github.ppodgorsek.juncacher.helper.InvalidationHelper;
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
	public void processEntries() {

		LOGGER.debug("Processing the invalidation entries");

		invalidationHelper.invalidateEntries();
	}

	protected InvalidationHelper<InvalidationEntry> getInvalidationHelper() {
		return invalidationHelper;
	}

	@Required
	public void setInvalidationHelper(final InvalidationHelper<InvalidationEntry> helper) {
		invalidationHelper = helper;
	}

}
