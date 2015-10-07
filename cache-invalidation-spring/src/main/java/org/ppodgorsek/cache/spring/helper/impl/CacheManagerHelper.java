package org.ppodgorsek.cache.spring.helper.impl;

import java.util.Map;

import org.ppodgorsek.cache.invalidation.exception.InvalidationException;
import org.ppodgorsek.cache.invalidation.helper.AbstractChainedInvalidationHelper;
import org.ppodgorsek.cache.invalidation.helper.InvalidationHelper;
import org.ppodgorsek.cache.invalidation.model.InvalidationEntry;
import org.ppodgorsek.cache.spring.strategy.CacheManagerStrategy;
import org.springframework.beans.factory.annotation.Required;

/**
 * Helper used to evict entries from a Spring Cache Manager.
 *
 * @since 1.0
 * @author Paul Podgorsek
 */
public class CacheManagerHelper<T extends InvalidationEntry>
		extends AbstractChainedInvalidationHelper<T>implements InvalidationHelper<T> {

	private Map<Class<T>, CacheManagerStrategy<T>> cacheManagerStrategies;

	@Override
	protected void invalidateEntry(final T entry) throws InvalidationException {

		final CacheManagerStrategy<T> cacheManagerStrategy = cacheManagerStrategies
				.get(entry.getClass());

		if (cacheManagerStrategy != null) {
			cacheManagerStrategy.invalidate(entry);
		}
	}

	public Map<Class<T>, CacheManagerStrategy<T>> getCacheManagerStrategies() {
		return cacheManagerStrategies;
	}

	@Required
	public void setCacheManagerStrategies(final Map<Class<T>, CacheManagerStrategy<T>> strategies) {
		cacheManagerStrategies = strategies;
	}

}
