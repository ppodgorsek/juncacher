package com.github.ppodgorsek.juncacher.helper;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import com.github.ppodgorsek.juncacher.exception.InvalidationException;
import com.github.ppodgorsek.juncacher.interceptor.InvalidationInterceptor;
import com.github.ppodgorsek.juncacher.logger.InvalidationLogger;
import com.github.ppodgorsek.juncacher.model.InvalidationEntry;
import com.github.ppodgorsek.juncacher.strategy.InvalidationStrategy;

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
public abstract class AbstractChainedInvalidationHelper<T extends InvalidationEntry, S extends InvalidationStrategy>
		implements InvalidationHelper<T> {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(AbstractChainedInvalidationHelper.class);

	private List<InvalidationInterceptor> interceptors;

	private InvalidationLogger<T> logger;

	private InvalidationHelper<T> nextHelper;

	private Map<String, S> strategies;

	@Override
	public void invalidateEntries() {

		InvalidationLogger<T> nextLogger = null;

		if (nextHelper != null) {
			nextLogger = nextHelper.getLogger();
		}

		try {
			preHandle();

			for (final T entry : logger.getEntries()) {
				invalidateEntry(entry, nextLogger);
			}

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
	 * Invalidates a cache entry.
	 *
	 * @param entry
	 *            The entry that must be invalidated.
	 * @param strategy
	 *            The strategy that must be used to invalidate the entry.
	 * @throws InvalidationException
	 *             An exception thrown if the invalidation couldn't be performed.
	 */
	protected abstract void invalidateEntry(T entry, S strategy) throws InvalidationException;

	/**
	 * Invalidates a cache entry.
	 *
	 * @param entry
	 *            The entry that must be invalidated.
	 * @param nextLogger
	 *            The logger to which the entry must be sent after having been invalidated by the
	 *            current helper.
	 */
	private void invalidateEntry(final T entry, final InvalidationLogger<T> nextLogger) {

		try {
			final S strategy = strategies.get(entry.getType().getValue());

			if (strategy == null) {
				LOGGER.info("No strategy found for entry {}", entry);
			}
			else {
				invalidateEntry(entry, strategy);
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

	public void setInterceptors(final List<InvalidationInterceptor> newInterceptors) {
		interceptors = newInterceptors;
	}

	@Override
	public InvalidationLogger<T> getLogger() {
		return logger;
	}

	@Required
	public void setLogger(final InvalidationLogger<T> newLogger) {
		logger = newLogger;
	}

	protected InvalidationHelper<T> getNextHelper() {
		return nextHelper;
	}

	public void setNextHelper(final InvalidationHelper<T> helper) {
		nextHelper = helper;
	}

	public Map<String, S> getStrategies() {
		return strategies;
	}

	@Required
	public void setStrategies(final Map<String, S> invalidationStrategies) {
		strategies = invalidationStrategies;
	}

}
