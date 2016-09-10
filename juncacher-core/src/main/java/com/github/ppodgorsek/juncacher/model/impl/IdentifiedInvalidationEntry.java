package com.github.ppodgorsek.juncacher.model.impl;

import org.springframework.util.Assert;

import com.github.ppodgorsek.juncacher.model.IdentifiableInvalidationEntry;
import com.github.ppodgorsek.juncacher.model.InvalidationEntryType;

/**
 * Invalidation entry that has a type and an identifier.
 *
 * @since 1.0
 * @author Paul Podgorsek
 */
public class IdentifiedInvalidationEntry extends TypedInvalidationEntry
		implements IdentifiableInvalidationEntry {

	private static final long serialVersionUID = -6026822219940792155L;

	private String referenceId;

	/**
	 * Default constructor allowing to set the reference's type and ID.
	 *
	 * @param referenceType
	 *            The reference's type.
	 * @param referenceId
	 *            The reference's ID.
	 */
	public IdentifiedInvalidationEntry(final InvalidationEntryType referenceType,
			final String referenceId) {

		super(referenceType);

		Assert.notNull(referenceId, "The reference ID is required");

		this.referenceId = referenceId;
	}

	@Override
	public boolean equals(final Object obj) {

		if (this == obj) {
			return true;
		}

		if (super.equals(obj) && obj instanceof IdentifiedInvalidationEntry) {

			final IdentifiedInvalidationEntry entry = (IdentifiedInvalidationEntry) obj;

			return referenceId.equals(entry.getReferenceId());
		}

		return false;
	}

	@Override
	public int hashCode() {
		return (getReferenceType() + "#" + referenceId).hashCode();
	}

	@Override
	public String toString() {

		final StringBuilder sbld = new StringBuilder(getClass().getSimpleName());
		sbld.append("[type=").append(getReferenceType());
		sbld.append(",id=").append(referenceId);
		sbld.append("]");

		return sbld.toString();
	}

	@Override
	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(final String newReferenceId) {

		Assert.notNull(newReferenceId, "The reference ID is required");

		referenceId = newReferenceId;
	}

}
