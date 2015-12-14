package com.github.ppodgorsek.cache.invalidation.processor.impl;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.github.ppodgorsek.cache.invalidation.helper.InvalidationHelper;
import com.github.ppodgorsek.cache.invalidation.model.InvalidationEntry;
import com.github.ppodgorsek.cache.invalidation.processor.InvalidationProcessor;

/**
 * An invalidation processor that triggers the invalidation from a helper.
 *
 * @since 1.0
 * @author Paul Podgorsek
 */
public class HelperInvalidationProcessor implements InvalidationProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(HelperInvalidationProcessor.class);

	private InvalidationHelper<InvalidationEntry> invalidationHelper;

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init(final ApplicationContext applicationContext) {

		if (invalidationHelper == null) {
			LOGGER.info("The invalidation helper hasn't been set, trying to determine a default one.");

			invalidationHelper = applicationContext.getBean(InvalidationHelper.class);

			LOGGER.info("Invalidation helper found in the application context, using it: {}",
					invalidationHelper);
		}
	}

	@Override
	public void processEntries() {

		LOGGER.debug("Processing the invalidation entries");

		invalidationHelper.invalidateEntries();
	}

	protected InvalidationHelper<InvalidationEntry> getInvalidationHelper() {
		return invalidationHelper;
	}

	public void setInvalidationHelper(final InvalidationHelper<InvalidationEntry> helper) {
		invalidationHelper = helper;
	}

}
