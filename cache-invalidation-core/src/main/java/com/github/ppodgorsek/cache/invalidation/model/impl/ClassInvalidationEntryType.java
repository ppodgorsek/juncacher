package com.github.ppodgorsek.cache.invalidation.model.impl;

import org.springframework.util.Assert;

import com.github.ppodgorsek.cache.invalidation.model.InvalidationEntryType;

/**
 * A type of entry based on a class.
 *
 * @since 1.0
 * @author Paul Podgorsek
 */
public class ClassInvalidationEntryType implements InvalidationEntryType {

	private static final long serialVersionUID = 2854493160724214863L;

	private final Class<?> clazz;

	/**
	 * Default constructor.
	 *
	 * @param clazz
	 *            The class to use to define this type.
	 */
	public ClassInvalidationEntryType(final Class<?> clazz) {

		super();

		Assert.notNull(clazz, "The class is required");

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
