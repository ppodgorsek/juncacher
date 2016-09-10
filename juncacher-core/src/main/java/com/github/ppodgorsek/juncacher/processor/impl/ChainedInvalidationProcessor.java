package com.github.ppodgorsek.juncacher.processor.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import com.github.ppodgorsek.juncacher.collector.InvalidationCollector;
import com.github.ppodgorsek.juncacher.exception.InvalidationException;
import com.github.ppodgorsek.juncacher.interceptor.InvalidationInterceptor;
import com.github.ppodgorsek.juncacher.model.InvalidationEntry;
import com.github.ppodgorsek.juncacher.processor.InvalidationProcessor;
import com.github.ppodgorsek.juncacher.strategy.InvalidationStrategy;

/**
 * {@link InvalidationProcessor} implementation allowing to chain processors to each other. This
 * could for example allow the following chain:
 * <ol>
 * <li>Spring Cache Manager</li>
 * <li>Solr</li>
 * <li>Varnish</li>
 * </ol>
 *
 * Child classes can override the invalidateEntry() and invalidateEntries() methods in order to have
 * a better control of the invalidation mechanism.
 *
 * @since 1.1
 * @author Paul Podgorsek
 */
public class ChainedInvalidationProcessor implements InvalidationProcessor {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ChainedInvalidationProcessor.class);

	private List<InvalidationInterceptor> interceptors;

	private InvalidationCollector collector;

	private InvalidationProcessor nextProcessor;

	private Map<String, InvalidationStrategy<InvalidationEntry>> strategies;

	@Override
	public void invalidateEntries() {

		InvalidationCollector nextCollector = null;

		if (nextProcessor != null) {
			nextCollector = nextProcessor.getCollector();
		}

		try {
			preHandle();
			invalidateEntries(collector.getEntries(), nextCollector);
			postHandle();
		}
		catch (final InvalidationException e) {
			LOGGER.warn("Impossible to perform a complete invalidation, will retry next time: {}",
					e.getMessage());
		}

		if (nextProcessor != null) {
			nextProcessor.invalidateEntries();
		}
	}

	/**
	 * Invalidates cache entries.
	 *
	 * @param entries
	 *            The entries that must be invalidated.
	 * @param nextCollector
	 *            The collector to which the entries must be sent after having been invalidated by
	 *            the current processor.
	 */
	protected void invalidateEntries(final List<InvalidationEntry> entries,
			final InvalidationCollector nextCollector) {

		for (final InvalidationEntry entry : entries) {
			invalidateEntry(entry, nextCollector);
		}
	}

	/**
	 * Invalidates a cache entry.
	 *
	 * @param entry
	 *            The entry that must be invalidated.
	 * @param nextCollector
	 *            The collector to which the entry must be sent after having been invalidated by the
	 *            current processor.
	 */
	protected void invalidateEntry(final InvalidationEntry entry,
			final InvalidationCollector nextCollector) {

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

			if (nextCollector != null) {
				nextCollector.addInvalidationEntry(entry);
			}

			collector.consume(entry);
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
	public InvalidationCollector getCollector() {
		return collector;
	}

	@Required
	public void setCollector(final InvalidationCollector newCollector) {
		collector = newCollector;
	}

	protected InvalidationProcessor getNextProcessor() {
		return nextProcessor;
	}

	public void setNextProcessor(final InvalidationProcessor processor) {
		nextProcessor = processor;
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
