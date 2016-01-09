package com.github.ppodgorsek.cache.invalidation.model.impl;

import com.github.ppodgorsek.cache.invalidation.model.InvalidationEntryType;

/**
 * A type of entry based on a class.
 *
 * @since 1.0
 * @author Paul Podgorsek
 */
public class ClassInvalidationEntryType implements InvalidationEntryType {

	private final Class<?> clazz;

	/**
	 * Default constructor.
	 * 
	 * @param clazz
	 *            The class to use to define this type.
	 */
	public ClassInvalidationEntryType(final Class<?> clazz) {
		super();
		this.clazz = clazz;
	}

	@Override
	public String getValue() {
		return clazz.getSimpleName();
	}

	@Override
	public String toString() {
		return getValue();
	}

}
