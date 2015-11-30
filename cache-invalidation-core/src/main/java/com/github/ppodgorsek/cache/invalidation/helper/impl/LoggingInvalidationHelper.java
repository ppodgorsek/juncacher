package com.github.ppodgorsek.cache.invalidation.helper.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.ppodgorsek.cache.invalidation.exception.InvalidationException;
import com.github.ppodgorsek.cache.invalidation.helper.AbstractChainedInvalidationHelper;
import com.github.ppodgorsek.cache.invalidation.helper.InvalidationHelper;
import com.github.ppodgorsek.cache.invalidation.model.InvalidationEntry;

/**
 * {@link InvalidationHelper} implementation that simply logs invalidation entries.
 *
 * @since 1.0
 * @author Paul Podgorsek
 */
public class LoggingInvalidationHelper<T extends InvalidationEntry> extends AbstractChainedInvalidationHelper<T> implements InvalidationHelper<T> {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoggingInvalidationHelper.class);

	@Override
	protected void invalidateEntry(final T entry) throws InvalidationException {
		LOGGER.info("Invalidating an entry: {}", entry);
	}

}
