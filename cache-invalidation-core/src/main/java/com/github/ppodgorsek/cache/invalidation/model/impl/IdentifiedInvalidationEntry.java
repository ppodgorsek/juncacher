package com.github.ppodgorsek.cache.invalidation.model.impl;

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

		type = entryType;
		id = entryId;
	}

	@Override
	public InvalidationEntryType getType() {
		return type;
	}

	public void setType(final InvalidationEntryType newType) {
		type = newType;
	}

	public String getId() {
		return id;
	}

	public void setId(final String newId) {
		id = newId;
	}

}
