package org.ppodgorsek.cache.invalidation.processor.impl;

import java.util.Collection;

import javax.annotation.PostConstruct;

import org.ppodgorsek.cache.invalidation.helper.InvalidationHelper;
import org.ppodgorsek.cache.invalidation.logger.InvalidationLogger;
import org.ppodgorsek.cache.invalidation.model.InvalidationEntry;
import org.ppodgorsek.cache.invalidation.processor.InvalidationProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

/**
 * An invalidation processor that reads entries from a logger.
 *
 * @author Paul Podgorsek
 */
public class LoggedInvalidationProcessor implements InvalidationProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoggedInvalidationProcessor.class);

	private InvalidationHelper<InvalidationEntry> invalidationHelper;

	private InvalidationLogger invalidationLogger;

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init(final ApplicationContext applicationContext) {

		if (invalidationHelper == null) {
			LOGGER.info("The invalidation helper hasn't been set, trying to determine a default one.");

			invalidationHelper = applicationContext.getBean(InvalidationHelper.class);

			LOGGER.info("Invalidation helper found in the application context, using it: {}", invalidationHelper);
		}

		if (invalidationLogger == null) {
			LOGGER.info("The invalidation logger hasn't been set, trying to determine a default one.");

			invalidationLogger = applicationContext.getBean(InvalidationLogger.class);

			LOGGER.info("Invalidation logger found in the application context, using it: {}", invalidationLogger);
		}
	}

	@Override
	public void processEntries() {

		LOGGER.debug("Processing the invalidation entries");

		final Collection<InvalidationEntry> entries = invalidationLogger.getEntries();

		invalidationHelper.invalidate(entries);
	}

	protected InvalidationHelper<InvalidationEntry> getInvalidationHelper() {
		return invalidationHelper;
	}

	public void setInvalidationHelper(final InvalidationHelper<InvalidationEntry> helper) {
		invalidationHelper = helper;
	}

	protected InvalidationLogger getInvalidationLogger() {
		return invalidationLogger;
	}

	public void setInvalidationLogger(final InvalidationLogger logger) {
		invalidationLogger = logger;
	}

}
