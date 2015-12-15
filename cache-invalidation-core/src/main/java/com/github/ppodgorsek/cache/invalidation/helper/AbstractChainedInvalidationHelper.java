package com.github.ppodgorsek.cache.invalidation.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import com.github.ppodgorsek.cache.invalidation.exception.InvalidationException;
import com.github.ppodgorsek.cache.invalidation.logger.InvalidationLogger;
import com.github.ppodgorsek.cache.invalidation.model.InvalidationEntry;

/**
 * Abstract {@link InvalidationHelper} implementation allowing to chain helpers to each other. This
 * could for example allow the following chain:
 * <ol>
 * <li>Spring Cache Manager</li>
 * <li>Solr</li>
 * <li>Varnish</li>
 * </ol>
 *
 * @since 1.0
 * @author Paul Podgorsek
 */
public abstract class AbstractChainedInvalidationHelper<T extends InvalidationEntry> implements
		InvalidationHelper<T> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(AbstractChainedInvalidationHelper.class);

	private InvalidationLogger<T> invalidationLogger;

	private InvalidationHelper<T> nextHelper;

	@Override
	public void invalidateEntries() {

		InvalidationLogger<T> nextLogger = null;

		if (nextHelper != null) {
			nextLogger = nextHelper.getInvalidationLogger();
		}

		for (final T entry : invalidationLogger.getEntries()) {
			try {
				invalidateEntry(entry);

				if (nextLogger != null) {
					nextLogger.addInvalidationEntry(entry);
				}

				invalidationLogger.consume(entry);
			}
			catch (final InvalidationException e) {
				LOGGER.warn(
						"Impossible to invalidate the {} entry, putting it back onto the queue of entries: {}",
						entry, e.getMessage());
			}
		}

		if (nextHelper != null) {
			nextHelper.invalidateEntries();
		}
	}

	/**
	 * Invalidate a cache entry.
	 *
	 * @param entry
	 *            The entry that must be invalidated.
	 * @throws InvalidationException
	 *             An exception thrown if the invalidation couldn't be performed.
	 */
	protected abstract void invalidateEntry(T entry) throws InvalidationException;

	@Override
	public InvalidationLogger<T> getInvalidationLogger() {
		return invalidationLogger;
	}

	@Required
	public void setInvalidationLogger(final InvalidationLogger<T> logger) {
		invalidationLogger = logger;
	}

	protected InvalidationHelper<T> getNextHelper() {
		return nextHelper;
	}

	public void setNextHelper(final InvalidationHelper<T> helper) {
		nextHelper = helper;
	}

}
