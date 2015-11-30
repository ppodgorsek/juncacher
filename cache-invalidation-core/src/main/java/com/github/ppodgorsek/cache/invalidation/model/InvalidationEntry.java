package com.github.ppodgorsek.cache.invalidation.model;

import java.io.Serializable;

/**
 * Holds an element that must be invalidated.
 *
 * @since 1.0
 * @author Paul Podgorsek
 */
public interface InvalidationEntry extends Serializable {

	/**
	 * @return The entry's type.
	 */
	InvalidationEntryType getType();

}
