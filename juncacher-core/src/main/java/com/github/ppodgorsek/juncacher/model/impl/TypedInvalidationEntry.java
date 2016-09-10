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

	private static final long serialVersionUID = 8000944776525026607L;

	private InvalidationEntryType referenceType;

	/**
	 * Default constructor allowing to set the reference type.
	 *
	 * @param referenceType
	 *            The reference's type.
	 */
	public TypedInvalidationEntry(final InvalidationEntryType referenceType) {

		super();

		Assert.notNull(referenceType, "The reference type is required");

		this.referenceType = referenceType;
	}

	@Override
	public boolean equals(final Object obj) {

		if (this == obj) {
			return true;
		}

		if (obj instanceof TypedInvalidationEntry) {

			final TypedInvalidationEntry entry = (TypedInvalidationEntry) obj;

			return referenceType.equals(entry.getReferenceType());
		}

		return false;
	}

	@Override
	public int hashCode() {
		return referenceType.hashCode();
	}

	@Override
	public String toString() {

		final StringBuilder sbld = new StringBuilder(getClass().getSimpleName());
		sbld.append("[type=").append(referenceType);
		sbld.append("]");

		return sbld.toString();
	}

	@Override
	public InvalidationEntryType getReferenceType() {
		return referenceType;
	}

	public void setReferenceType(final InvalidationEntryType newType) {

		Assert.notNull(newType, "The reference type is required");

		referenceType = newType;
	}

}
