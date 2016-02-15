package com.github.ppodgorsek.juncacher.model.impl;

import org.springframework.util.Assert;

import com.github.ppodgorsek.juncacher.model.InvalidationEntry;
import com.github.ppodgorsek.juncacher.model.InvalidationEntryType;

/**
 * Invalidation entry that only has a type.
 *
 * @since 1.0
 * @author Paul Podgorsek
 */
public class TypedInvalidationEntry implements InvalidationEntry {

	private static final long serialVersionUID = -454660306174964926L;

	private InvalidationEntryType type;

	/**
	 * Default constructor allowing to set the type.
	 *
	 * @param entryType
	 *            The entry's type.
	 */
	public TypedInvalidationEntry(final InvalidationEntryType entryType) {

		super();

		Assert.notNull(entryType, "The type is required");

		type = entryType;
	}

	@Override
	public boolean equals(final Object obj) {

		if (this == obj) {
			return true;
		}

		if (obj instanceof TypedInvalidationEntry) {

			final TypedInvalidationEntry entry = (TypedInvalidationEntry) obj;

			return type.equals(entry.getType());
		}

		return false;
	}

	@Override
	public int hashCode() {
		return type.hashCode();
	}

	@Override
	public String toString() {

		final StringBuilder sbld = new StringBuilder(getClass().getSimpleName());
		sbld.append("[type=").append(type);
		sbld.append("]");

		return sbld.toString();
	}

	@Override
	public InvalidationEntryType getType() {
		return type;
	}

	public void setType(final InvalidationEntryType newType) {

		Assert.notNull(newType, "The type is required");

		type = newType;
	}

}
