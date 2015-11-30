package com.github.ppodgorsek.cache.invalidation.model.impl;

import com.github.ppodgorsek.cache.invalidation.model.InvalidationEntryType;

/**
 * A default enumeration of types of entries.
 *
 * @since 1.0
 * @author Paul Podgorsek
 */
public enum DefaultInvalidationEntryType implements InvalidationEntryType {

	GLOBAL;

	@Override
	public String getValue() {
		return toString();
	}

}
