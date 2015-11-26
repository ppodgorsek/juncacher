package com.github.ppodgorsek.cache.invalidation.processor;

/**
 * Processes invalidation entries that were previously created. These entries could be for example
 * read from a logger or directly from a database.
 *
 * @since 1.0
 * @author Paul Podgorsek
 */
public interface InvalidationProcessor {

	/**
	 * Process the invalidation entries which already exist.
	 */
	void processEntries();

}
