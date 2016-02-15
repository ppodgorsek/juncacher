package com.github.ppodgorsek.juncacher.model.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.github.ppodgorsek.juncacher.model.InvalidationEntryType;

/**
 * Tests for the {@link TypedInvalidationEntry} class.
 *
 * @author Paul Podgorsek
 */
public class TypedInvalidationEntryTest {

	private final TypedInvalidationEntry typedInvalidationEntry = new TypedInvalidationEntry(
			DefaultInvalidationEntryType.GLOBAL);

	@Test
	public void constructWithCorrectType() {
		new TypedInvalidationEntry(DefaultInvalidationEntryType.GLOBAL);
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructWithNullType() {
		new TypedInvalidationEntry(null);
	}

	@Test
	public void equalsWithSameInstance() {
		assertTrue("The equals() method should return true",
				typedInvalidationEntry.equals(typedInvalidationEntry));
	}

	@Test
	public void equalsWithDifferentClass() {
		assertFalse("The equals() method should return false",
				typedInvalidationEntry.equals(new Object()));
	}

	@Test
	public void equalsWithDifferentInstanceHavingSameType() {

		final TypedInvalidationEntry otherEntry = new TypedInvalidationEntry(
				DefaultInvalidationEntryType.GLOBAL);

		assertTrue("The equals() method should return true",
				typedInvalidationEntry.equals(otherEntry));
	}

	@Test
	public void equalsWithDifferentInstanceHavingDifferentType() {

		final TypedInvalidationEntry otherEntry = new TypedInvalidationEntry(
				new ClassInvalidationEntryType(Object.class));

		assertFalse("The equals() method should return false",
				typedInvalidationEntry.equals(otherEntry));
	}

	@Test
	public void hashCodeWithCorrectValue() {

		final int hashCode = typedInvalidationEntry.hashCode();

		assertTrue("Wrong hashCode", hashCode != 0);
	}

	@Test
	public void toStringWithCorrectValues() {

		final StringBuilder sbld = new StringBuilder();
		sbld.append("TypedInvalidationEntry[type=");
		sbld.append(DefaultInvalidationEntryType.GLOBAL);
		sbld.append("]");

		final String toStringValue = typedInvalidationEntry.toString();

		assertNotNull("The toString() value shouldn't be null", toStringValue);
		assertEquals("Wrong toString() value", sbld.toString(), toStringValue);
	}

	@Test
	public void typeGetterSetterWithCorrectValue() {

		final InvalidationEntryType newType = new ClassInvalidationEntryType(getClass());

		typedInvalidationEntry.setType(newType);

		final InvalidationEntryType type = typedInvalidationEntry.getType();

		assertNotNull("The type shouldn't be null", type);
		assertEquals("Wrong type", newType, type);
	}

	@Test(expected = IllegalArgumentException.class)
	public void typeGetterSetterWithNullValue() {
		typedInvalidationEntry.setType(null);
	}

}
