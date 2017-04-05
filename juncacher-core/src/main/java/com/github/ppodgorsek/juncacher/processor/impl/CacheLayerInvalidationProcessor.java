package com.github.ppodgorsek.juncacher.processor.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import com.github.ppodgorsek.juncacher.exception.InvalidationException;
import com.github.ppodgorsek.juncacher.interceptor.InvalidationInterceptor;
import com.github.ppodgorsek.juncacher.model.InvalidationEntry;
import com.github.ppodgorsek.juncacher.processor.InvalidationProcessor;
import com.github.ppodgorsek.juncacher.strategy.InvalidationStrategy;

/**
 * {@link InvalidationProcessor} implementation allowing to process a single layer of cache. This
 * could for example be used for one of the following layers:
 * <ul>
 * <li>Spring Cache Manager</li>
 * <li>Solr</li>
 * <li>Varnish</li>
 * </ul>
 *
 * @since 1.2
 * @author Paul Podgorsek
 */
public class CacheLayerInvalidationProcessor implements InvalidationProcessor {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(CacheLayerInvalidationProcessor.class);

	private List<InvalidationInterceptor> interceptors;

	private Map<String, InvalidationStrategy<InvalidationEntry>> strategies;

	@Override
	public Collection<InvalidationEntry> invalidateEntries(
			final Collection<InvalidationEntry> entries) {

		final List<InvalidationEntry> invalidatedEntries = new ArrayList<>();

		try {
			preHandle();

			for (final InvalidationEntry entry : entries) {
				final InvalidationEntry invalidatedEntry = invalidateEntry(entry);

				if (invalidatedEntry != null) {
					invalidatedEntries.add(invalidatedEntry);
				}
			}

			postHandle();
		}
		catch (final InvalidationException e) {
			LOGGER.warn("Impossible to perform a complete invalidation, will retry next time: {}",
					e.getMessage());
		}

		return invalidatedEntries;
	}

	/**
	 * Invalidates a cache entry.
	 *
	 * @param entry
	 *            The entry that must be invalidated.
	 * @return The invalidated entry, or {@code null} if it wasn't invalidated.
	 */
	protected InvalidationEntry invalidateEntry(final InvalidationEntry entry) {

		Assert.notNull(entry, "The entry is required");

		LOGGER.debug("Invalidating an entry: {}", entry);

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
					LOGGER.error("The {} strategy can't handle the entry: {}", strategy, entry);
				}
			}

			return entry;
		}
		catch (final InvalidationException e) {
			LOGGER.warn("Impossible to invalidate the {} entry, will retry next time: {}", entry,
					e.getMessage());
		}

		return null;
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

	public Map<String, InvalidationStrategy<InvalidationEntry>> getStrategies() {
		return strategies;
	}

	@Required
	public void setStrategies(
			final Map<String, InvalidationStrategy<InvalidationEntry>> invalidationStrategies) {
		strategies = invalidationStrategies;
	}

}
