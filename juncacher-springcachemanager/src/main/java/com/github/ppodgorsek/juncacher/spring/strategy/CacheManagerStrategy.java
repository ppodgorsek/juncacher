package com.github.ppodgorsek.juncacher.spring.strategy;

import org.springframework.cache.CacheManager;

import com.github.ppodgorsek.juncacher.exception.InvalidationException;
import com.github.ppodgorsek.juncacher.model.InvalidationEntry;
import com.github.ppodgorsek.juncacher.strategy.InvalidationStrategy;

/**
 * Strategy used to determines which elements should be removed from the {@link CacheManager}.
 *
 * @since 1.0
 * @author Paul Podgorsek
 */
public interface CacheManagerStrategy<T extends InvalidationEntry> extends InvalidationStrategy {

	/**
	 * Evicts an entry from the cache.
	 *
	 * @param entry
	 *            The entry that must be invalidated.
	 */
	void evict(T entry) throws InvalidationException;

}
