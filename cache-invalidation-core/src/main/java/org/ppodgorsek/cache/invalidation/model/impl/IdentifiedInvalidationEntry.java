package org.ppodgorsek.cache.invalidation.model.impl;

import org.ppodgorsek.cache.invalidation.model.InvalidationEntry;

/**
 * Invalidation entry that has an identifier.
 * 
 * @author Paul Podgorsek
 */
public class IdentifiedInvalidationEntry implements InvalidationEntry {

	private static final long serialVersionUID = -8341534831781291865L;

	private String id;

	/**
	 * Default constructor.
	 */
	public IdentifiedInvalidationEntry() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(final String newId) {
		id = newId;
	}

}
