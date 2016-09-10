package com.github.ppodgorsek.juncacher.model;

import java.io.Serializable;

/**
 * Holds an element that must be invalidated.
 *
 * @since 1.0
 * @author Paul Podgorsek
 */
public interface InvalidationEntry extends Serializable {

	/**
	 * @return The type of object referenced by the entry.
	 */
	InvalidationEntryType getReferenceType();

}
