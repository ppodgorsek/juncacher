package com.github.ppodgorsek.juncacher.handler;

import java.util.Set;

import com.github.ppodgorsek.juncacher.model.InvalidationEntry;

/**
 * Handler allowing to fetch the invalidation entries related to an object which has been modified
 * or deleted.
 *
 * @since 1.0
 * @author Paul Podgorsek
 */
public interface InvalidationHandler<T> {

	/**
	 * Get the invalidation entries related to an object that has changed.
	 *
	 * @param object
	 *            The object that has changed.
	 * @return The elements related to the provided object, or an empty set if there are none.
	 */
	Set<InvalidationEntry> getRelatedEntries(T object);

}
