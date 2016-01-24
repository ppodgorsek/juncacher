package com.github.ppodgorsek.juncacher.helper.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.ppodgorsek.juncacher.exception.InvalidationException;
import com.github.ppodgorsek.juncacher.helper.AbstractChainedInvalidationHelper;
import com.github.ppodgorsek.juncacher.helper.InvalidationHelper;
import com.github.ppodgorsek.juncacher.model.InvalidationEntry;
import com.github.ppodgorsek.juncacher.strategy.InvalidationStrategy;

/**
 * {@link InvalidationHelper} implementation that simply logs invalidation entries.
 *
 * @since 1.0
 * @author Paul Podgorsek
 */
public class LoggingInvalidationHelper<T extends InvalidationEntry>
		extends AbstractChainedInvalidationHelper<T, InvalidationStrategy>
		implements InvalidationHelper<T> {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoggingInvalidationHelper.class);

	@Override
	protected void invalidateEntry(final T entry, final InvalidationStrategy strategy)
			throws InvalidationException {
		LOGGER.info("Invalidating an entry: {}", entry);
	}

}
