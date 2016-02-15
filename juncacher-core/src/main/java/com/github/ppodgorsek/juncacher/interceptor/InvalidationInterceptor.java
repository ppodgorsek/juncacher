package com.github.ppodgorsek.juncacher.interceptor;

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
	 */
	void preHandle();

	/**
	 * Performs the actions required after the invalidation happens.
	 */
	void postHandle();

}
