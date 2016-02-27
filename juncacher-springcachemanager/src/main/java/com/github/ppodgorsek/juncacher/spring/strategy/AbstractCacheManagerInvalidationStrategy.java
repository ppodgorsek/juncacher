package com.github.ppodgorsek.juncacher.spring.strategy;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.cache.CacheManager;

import com.github.ppodgorsek.juncacher.model.InvalidationEntry;
import com.github.ppodgorsek.juncacher.strategy.InvalidationStrategy;

/**
 * Strategy used to determine which elements should be removed from the {@link CacheManager}. It
 * contains the cache region names for a type of invalidation entry.
 *
 * @since 1.0
 * @author Paul Podgorsek
 */
public abstract class AbstractCacheManagerInvalidationStrategy<T extends InvalidationEntry>
		implements InvalidationStrategy<T> {

	private CacheManager cacheManager;

	private List<String> cacheNames;

	public CacheManager getCacheManager() {
		return cacheManager;
	}

	@Required
	public void setCacheManager(final CacheManager manager) {
		cacheManager = manager;
	}

	public List<String> getCacheNames() {
		return cacheNames;
	}

	@Required
	public void setCacheNames(final List<String> names) {
		cacheNames = names;
	}

}
