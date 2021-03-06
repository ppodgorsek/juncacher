package com.github.ppodgorsek.juncacher.model;

import java.io.Serializable;

/**
 * Each invalidation entry has a type, this allows to easily identify which strategy to apply for
 * it. Explicitely defining a type rather than relying on the type of instances avoids creating a
 * big set of identical classes.
 *
 * @since 1.0
 * @author Paul Podgorsek
 */
public interface InvalidationEntryType extends Serializable {

	/**
	 * @return The value of the type of entry.
	 */
	String getValue();

}
