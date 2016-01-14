package com.github.ppodgorsek.juncacher.model.impl;

import com.github.ppodgorsek.juncacher.model.InvalidationEntryType;

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
