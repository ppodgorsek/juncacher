package com.github.ppodgorsek.cache.invalidation.model.impl;

import org.springframework.util.Assert;

import com.github.ppodgorsek.cache.invalidation.model.InvalidationEntry;
import com.github.ppodgorsek.cache.invalidation.model.InvalidationEntryType;

/**
 * Invalidation entry that has an identifier.
 *
 * @since 1.0
 * @author Paul Podgorsek
 */
public class IdentifiedInvalidationEntry implements InvalidationEntry {

	private static final long serialVersionUID = -1324699893292434964L;

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
	public IdentifiedInvalidationEntry(final InvalidationEntryType entryType, final String entryId) {

		super();

		Assert.notNull(entryType, "The type is required");
		Assert.notNull(entryId, "The ID is required");

		type = entryType;
		id = entryId;
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
