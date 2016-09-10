package com.github.ppodgorsek.juncacher.model.impl;

import org.springframework.util.Assert;

import com.github.ppodgorsek.juncacher.model.InvalidationEntryType;

/**
 * A simple type for invalidation entries.
 *
 * @since 1.0
 * @author Paul Podgorsek
 */
public class SimpleInvalidationEntryType implements InvalidationEntryType {

	private static final long serialVersionUID = -9218451001053136429L;

	private final String value;

	/**
	 * Default constructor allowing to set the value.
	 *
	 * @param value
	 *            The type's value.
	 */
	public SimpleInvalidationEntryType(final String value) {

		super();

		Assert.notNull(value, "The value is required");

		this.value = value;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		return getValue().hashCode();
	}

	@Override
	public String toString() {
		return getValue();
	}

}
