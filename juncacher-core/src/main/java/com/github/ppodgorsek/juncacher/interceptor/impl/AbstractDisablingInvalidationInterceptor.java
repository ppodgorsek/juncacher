package com.github.ppodgorsek.juncacher.interceptor.impl;

import com.github.ppodgorsek.juncacher.exception.InvalidationException;
import com.github.ppodgorsek.juncacher.interceptor.InvalidationInterceptor;

/**
 * Interceptor allowing to stop processors from handling entries in case the invalidation has been
 * disabled. By using interceptors, the invalidation can be easily enabled or disabled, as the
 * verification will be run every time the invalidation is triggered. Besides, some layers of cache
 * could be invalidated while others are disabled.
 *
 * @since 1.1
 * @author Paul Podgorsek
 */
public abstract class AbstractDisablingInvalidationInterceptor implements InvalidationInterceptor {

	@Override
	public void preHandle() throws InvalidationException {
		if (isInvalidationDisabled()) {
			throw new InvalidationException(
					"The invalidation has been disabled, no further processing should be done.");
		}
	}

	@Override
	public void postHandle() throws InvalidationException {
		if (isInvalidationDisabled()) {
			throw new InvalidationException(
					"The invalidation has been disabled, no further processing should be done.");
		}
	}

	/**
	 * Checks if the invalidation is disabled or not.
	 *
	 * @return {@code true} if the invalidation has been disabled, {@code false} otherwise.
	 */
	protected abstract boolean isInvalidationDisabled();

}
