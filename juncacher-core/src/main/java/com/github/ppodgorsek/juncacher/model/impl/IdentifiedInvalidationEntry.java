package com.github.ppodgorsek.juncacher.model.impl;

import org.springframework.util.Assert;

import com.github.ppodgorsek.juncacher.model.InvalidationEntry;
import com.github.ppodgorsek.juncacher.model.InvalidationEntryType;

/**
 * Invalidation entry that has a type and an identifier.
 *
 * @since 1.0
 * @author Paul Podgorsek
 */
public class IdentifiedInvalidationEntry extends TypedInvalidationEntry
		implements InvalidationEntry {

	private static final long serialVersionUID = 4306519736253591118L;

	private String id;

	/**
	 * Default constructor allowing to set the type and ID.
	 *
	 * @param entryType
	 *            The entry's type.
	 * @param entryId
	 *            The entry's ID.
	 */
	public IdentifiedInvalidationEntry(final InvalidationEntryType entryType,
			final String entryId) {

		super(entryType);

		Assert.notNull(entryId, "The ID is required");

		id = entryId;
	}

	@Override
	public boolean equals(final Object obj) {

		if (this == obj) {
			return true;
		}

		if (super.equals(obj) && obj instanceof IdentifiedInvalidationEntry) {

			final IdentifiedInvalidationEntry entry = (IdentifiedInvalidationEntry) obj;

			return id.equals(entry.getId());
		}

		return false;
	}

	@Override
	public int hashCode() {
		return (getType() + "#" + id).hashCode();
	}

	@Override
	public String toString() {

		final StringBuilder sbld = new StringBuilder(getClass().getSimpleName());
		sbld.append("[type=").append(getType());
		sbld.append(",id=").append(id);
		sbld.append("]");

		return sbld.toString();
	}

	public String getId() {
		return id;
	}

	public void setId(final String newId) {

		Assert.notNull(newId, "The ID is required");

		id = newId;
	}

}
