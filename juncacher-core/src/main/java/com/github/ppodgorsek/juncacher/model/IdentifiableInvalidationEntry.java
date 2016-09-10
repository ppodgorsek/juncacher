package com.github.ppodgorsek.juncacher.model;

/**
 * Holds an element that must be invalidated.
 *
 * @since 1.0
 * @author Paul Podgorsek
 */
public interface IdentifiableInvalidationEntry extends InvalidationEntry {

	/**
	 * @return The ID of the object referenced by the entry.
	 */
	String getReferenceId();

}
