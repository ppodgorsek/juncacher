package org.ppodgorsek.cache.spring.strategy;

import java.util.List;

import org.ppodgorsek.cache.invalidation.model.InvalidationEntry;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.cache.CacheManager;

/**
 * @author Paul Podgorsek
 */
public abstract class AbstractCacheManagerInvalidationStrategy<T extends InvalidationEntry> implements CacheManagerStrategy<T> {

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
