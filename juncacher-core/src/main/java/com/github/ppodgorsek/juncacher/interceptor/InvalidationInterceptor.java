package com.github.ppodgorsek.juncacher.interceptor;

import com.github.ppodgorsek.juncacher.exception.InvalidationException;

/**
 * Interceptor called before and after an invalidation is triggered by a helper (before/after all
 * entries are invalidated, not before/after each individual one).
 *
 * @since 1.0
 * @author Paul Podgorsek
 */
public interface InvalidationInterceptor {

	/**
	 * Performs the actions required before the invalidation happens.
	 *
	 * @throws InvalidationException
	 *             An exception thrown by pre-invalidation checks.
	 */
	void preHandle() throws InvalidationException;

	/**
	 * Performs the actions required after the invalidation happens.
	 *
	 * @throws InvalidationException
	 *             An exception thrown by post-invalidation checks.
	 */
	void postHandle() throws InvalidationException;

}
