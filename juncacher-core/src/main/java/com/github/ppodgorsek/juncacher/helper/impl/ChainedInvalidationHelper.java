package com.github.ppodgorsek.juncacher.helper.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import com.github.ppodgorsek.juncacher.exception.InvalidationException;
import com.github.ppodgorsek.juncacher.helper.InvalidationHelper;
import com.github.ppodgorsek.juncacher.interceptor.InvalidationInterceptor;
import com.github.ppodgorsek.juncacher.logger.InvalidationLogger;
import com.github.ppodgorsek.juncacher.model.InvalidationEntry;
import com.github.ppodgorsek.juncacher.strategy.InvalidationStrategy;

/**
 * {@link InvalidationHelper} implementation allowing to chain helpers to each other. This could for
 * example allow the following chain:
 * <ol>
 * <li>Spring Cache Manager</li>
 * <li>Solr</li>
 * <li>Varnish</li>
 * </ol>
 *
 * Child classes can override the invalidateEntry() and invalidateEntries() methods in order to have
 * a better control of the invalidation mechanism.
 *
 * @since 1.0
 * @author Paul Podgorsek
 */
public class ChainedInvalidationHelper implements InvalidationHelper {

	private static final Logger LOGGER = LoggerFactory.getLogger(ChainedInvalidationHelper.class);

	private List<InvalidationInterceptor> interceptors;

	private InvalidationLogger logger;

	private InvalidationHelper nextHelper;

	private Map<String, InvalidationStrategy<InvalidationEntry>> strategies;

	@Override
	public void invalidateEntries() {

		InvalidationLogger nextLogger = null;

		if (nextHelper != null) {
			nextLogger = nextHelper.getLogger();
		}

		try {
			preHandle();
			invalidateEntries(logger.getEntries(), nextLogger);
			postHandle();
		}
		catch (final InvalidationException e) {
			LOGGER.warn("Impossible to perform a complete invalidation, will retry next time: {}",
					e.getMessage());
		}

		if (nextHelper != null) {
			nextHelper.invalidateEntries();
		}
	}

	/**
	 * Invalidates cache entries.
	 *
	 * @param entries
	 *            The entries that must be invalidated.
	 * @param nextLogger
	 *            The logger to which the entry must be sent after having been invalidated by the
	 *            current helper.
	 */
	protected void invalidateEntries(final List<InvalidationEntry> entries,
			final InvalidationLogger nextLogger) {

		for (final InvalidationEntry entry : entries) {
			invalidateEntry(entry, nextLogger);
		}
	}

	/**
	 * Invalidates a cache entry.
	 *
	 * @param entry
	 *            The entry that must be invalidated.
	 * @param nextLogger
	 *            The logger to which the entry must be sent after having been invalidated by the
	 *            current helper.
	 */
	protected void invalidateEntry(final InvalidationEntry entry,
			final InvalidationLogger nextLogger) {

		LOGGER.info("Invalidating an entry: {}", entry);

		try {
			final InvalidationStrategy<InvalidationEntry> strategy = strategies
					.get(entry.getReferenceType().getValue());

			if (strategy == null) {
				LOGGER.info("No strategy found for entry {}", entry);
			}
			else {
				if (strategy.canHandle(entry)) {
					strategy.invalidate(entry);
				}
				else {
					LOGGER.warn("The {} strategy can't handle the entry: {}", strategy, entry);
				}
			}

			if (nextLogger != null) {
				nextLogger.addInvalidationEntry(entry);
			}

			logger.consume(entry);
		}
		catch (final InvalidationException e) {
			LOGGER.warn("Impossible to invalidate the {} entry, will retry next time: {}", entry,
					e.getMessage());
		}
	}

	/**
	 * Performs the actions required before the invalidation happens.
	 *
	 * @throws InvalidationException
	 *             An exception thrown by pre-invalidation checks.
	 */
	private void preHandle() throws InvalidationException {

		if (interceptors != null) {
			for (final InvalidationInterceptor interceptor : interceptors) {
				interceptor.preHandle();
			}
		}
	}

	/**
	 * Performs the actions required after the invalidation happens.
	 *
	 * @throws InvalidationException
	 *             An exception thrown by post-invalidation checks.
	 */
	private void postHandle() throws InvalidationException {

		if (interceptors != null) {
			for (final InvalidationInterceptor interceptor : interceptors) {
				interceptor.postHandle();
			}
		}
	}

	public List<InvalidationInterceptor> getInterceptors() {
		return interceptors;
	}

	public void setInterceptors(final List<InvalidationInterceptor> newInterceptors) {
		interceptors = newInterceptors;
	}

	@Override
	public InvalidationLogger getLogger() {
		return logger;
	}

	@Required
	public void setLogger(final InvalidationLogger newLogger) {
		logger = newLogger;
	}

	protected InvalidationHelper getNextHelper() {
		return nextHelper;
	}

	public void setNextHelper(final InvalidationHelper helper) {
		nextHelper = helper;
	}

	public Map<String, InvalidationStrategy<InvalidationEntry>> getStrategies() {
		return strategies;
	}

	@Required
	public void setStrategies(
			final Map<String, InvalidationStrategy<InvalidationEntry>> invalidationStrategies) {
		strategies = invalidationStrategies;
	}

}
