package org.ppodgorsek.cache.invalidation.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;

import org.ppodgorsek.cache.invalidation.exception.InvalidationException;
import org.ppodgorsek.cache.invalidation.logger.InvalidationLogger;
import org.ppodgorsek.cache.invalidation.model.InvalidationEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

/**
 * @author Paul Podgorsek
 */
public abstract class AbstractChainedInvalidationHelper<T extends InvalidationEntry> implements InvalidationHelper<T> {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractChainedInvalidationHelper.class);

	private InvalidationLogger invalidationLogger;

	private InvalidationHelper<T> nextHelper;

	@PostConstruct
	public void init(final ApplicationContext applicationContext) {

		if (invalidationLogger == null) {
			LOGGER.info("The invalidation logger hasn't been set, trying to determine a default one.");

			invalidationLogger = applicationContext.getBean(InvalidationLogger.class);

			LOGGER.info("Invalidation logger found in the application context, using it: {}", invalidationLogger);
		}
	}

	@Override
	public void invalidate(final Collection<T> entries) {

		final List<T> processedEntries = new ArrayList<>();

		for (final T entry : entries) {
			try {
				invalidateEntry(entry);
				processedEntries.add(entry);
			}
			catch (final InvalidationException e) {
				LOGGER.warn("Impossible to invalidate the {} entry, putting it back onto the queue of entries: {}", entry, e.getMessage());
				invalidationLogger.addInvalidationEntry(entry);
			}
		}

		getNextHelper().invalidate(processedEntries);
	}

	/**
	 * Invalidates a cache entry.
	 *
	 * @param entry
	 *            The entry that must be invalidated.
	 * @throws InvalidationException
	 *             An exception thrown if the invalidation couldn't be performed.
	 */
	protected abstract void invalidateEntry(T entry) throws InvalidationException;

	public void setInvalidationLogger(final InvalidationLogger logger) {
		invalidationLogger = logger;
	}

	protected InvalidationHelper<T> getNextHelper() {
		return nextHelper;
	}

	public void setNextHelper(final InvalidationHelper<T> helper) {
		nextHelper = helper;
	}

}
