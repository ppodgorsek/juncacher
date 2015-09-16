package org.ppodgorsek.cache.invalidation.processor;

/**
 * Processes invalidation entries that were previously created. These entries could be for example read from a logger or directly from a database.
 *
 * @author Paul Podgorsek
 */
public interface InvalidationProcessor {

	/**
	 * Processes the invalidation entries which already exist.
	 */
	void processEntries();

}
