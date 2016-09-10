package com.github.ppodgorsek.juncacher.helper;

import com.github.ppodgorsek.juncacher.logger.InvalidationLogger;

/**
 * Helper that invalidates entries.
 *
 * @since 1.0
 * @author Paul Podgorsek
 */
public interface InvalidationHelper {

	/**
	 * Returns the invalidation logger attached to this helper.
	 *
	 * @return The logger attached to this helper.
	 */
	InvalidationLogger getLogger();

	/**
	 * Invalidates the cache entries read from the invalidation logger.
	 */
	void invalidateEntries();

}
