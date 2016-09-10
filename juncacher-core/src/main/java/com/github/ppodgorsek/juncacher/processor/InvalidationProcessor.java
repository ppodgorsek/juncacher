package com.github.ppodgorsek.juncacher.processor;

import com.github.ppodgorsek.juncacher.collector.InvalidationCollector;

/**
 * Processor that invalidates entries.
 *
 * @since 1.1
 * @author Paul Podgorsek
 */
public interface InvalidationProcessor {

	/**
	 * Returns the invalidation collector attached to this processor.
	 *
	 * @return The collector attached to this processor.
	 */
	InvalidationCollector getCollector();

	/**
	 * Invalidates the cache entries read from the invalidation collector.
	 */
	void invalidateEntries();

}
