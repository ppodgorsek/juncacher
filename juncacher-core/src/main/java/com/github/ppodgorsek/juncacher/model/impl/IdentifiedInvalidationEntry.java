package com.github.ppodgorsek.juncacher.model.impl;

import org.springframework.util.Assert;

import com.github.ppodgorsek.juncacher.model.InvalidationEntry;
import com.github.ppodgorsek.juncacher.model.InvalidationEntryType;

/**
 * Invalidation entry that has an identifier.
 *
 * @since 1.0
 * @author Paul Podgorsek
 */
public class IdentifiedInvalidationEntry implements InvalidationEntry {

	private static final long serialVersionUID = 5096472886714074748L;

	private InvalidationEntryType type;

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

		super();

		Assert.notNull(entryType, "The type is required");
		Assert.notNull(entryId, "The ID is required");

		type = entryType;
		id = entryId;
	}

	@Override
	public boolean equals(final Object obj) {

		if (this == obj) {
			return true;
		}

		if (obj instanceof IdentifiedInvalidationEntry) {

			final IdentifiedInvalidationEntry entryType = (IdentifiedInvalidationEntry) obj;

			return type.equals(entryType.getType()) && id.equals(entryType.getId());
		}

		return false;
	}

	@Override
	public int hashCode() {
		return (type + "#" + id).hashCode();
	}

	@Override
	public String toString() {

		final StringBuilder sbld = new StringBuilder(getClass().getSimpleName());
		sbld.append("[type=").append(type);
		sbld.append(",id=").append(id);
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

	public String getId() {
		return id;
	}

	public void setId(final String newId) {

		Assert.notNull(newId, "The ID is required");

		id = newId;
	}

}
