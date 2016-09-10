package com.github.ppodgorsek.juncacher.model.impl;

import com.github.ppodgorsek.juncacher.model.InvalidationEntryType;

/**
 * A type of entry based on a class.
 *
 * @since 1.0
 * @author Paul Podgorsek
 */
public class ClassInvalidationEntryType extends SimpleInvalidationEntryType
		implements InvalidationEntryType {

	private static final long serialVersionUID = -67680493220554287L;

	/**
	 * Default constructor.
	 *
	 * @param clazz
	 *            The class to use to define this type.
	 */
	public ClassInvalidationEntryType(final Class<?> clazz) {
		super(clazz.getSimpleName());
	}

	@Override
	public boolean equals(final Object obj) {

		if (this == obj) {
			return true;
		}

		if (obj instanceof ClassInvalidationEntryType) {

			final ClassInvalidationEntryType entryType = (ClassInvalidationEntryType) obj;

			return getValue().equals(entryType.getValue());
		}

		return false;
	}

}
