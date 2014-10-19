package org.ppodgorsek.cache.invalidation.strategy.impl;

import java.util.List;

import org.ppodgorsek.cache.invalidation.exception.InvalidationException;
import org.ppodgorsek.cache.invalidation.helper.InvalidationHelper;
import org.ppodgorsek.cache.invalidation.model.InvalidationEntry;
import org.ppodgorsek.cache.invalidation.strategy.InvalidationStrategy;
import org.springframework.beans.factory.annotation.Required;

/**
 * Abstract strategy that invalidates entries by sending them to delegate helpers.
 * 
 * @author Paul Podgorsek
 */
public abstract class AbstractInvalidationStrategy<T extends InvalidationEntry> implements InvalidationStrategy<T> {

	private List<InvalidationHelper> helpers;

	/**
	 * Default constructor.
	 */
	public AbstractInvalidationStrategy() {
		super();
	}

	@Override
	public void delegateInvalidation(final T entry) throws InvalidationException {

		for (InvalidationHelper helper : helpers) {
			helper.invalidateEntry(entry);
		}
	}

	public List<InvalidationHelper> getHelpers() {
		return helpers;
	}

	@Required
	public void setHelpers(final List<InvalidationHelper> newHelpers) {
		helpers = newHelpers;
	}

}
