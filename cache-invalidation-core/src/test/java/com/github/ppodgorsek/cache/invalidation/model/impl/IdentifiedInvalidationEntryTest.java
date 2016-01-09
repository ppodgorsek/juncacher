package com.github.ppodgorsek.cache.invalidation.model.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.github.ppodgorsek.cache.invalidation.model.InvalidationEntryType;

/**
 * Tests for the {@link IdentifiedInvalidationEntry} class.
 *
 * @author Paul Podgorsek
 */
public class IdentifiedInvalidationEntryTest {

	private final IdentifiedInvalidationEntry identifiedInvalidationEntry = new IdentifiedInvalidationEntry(
			DefaultInvalidationEntryType.GLOBAL, "testId");

	@Test
	public void constructWithCorrectTypeAndCorrectId() {
		new IdentifiedInvalidationEntry(DefaultInvalidationEntryType.GLOBAL, "testId");
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructWithCorrectTypeAndNullId() {
		new IdentifiedInvalidationEntry(DefaultInvalidationEntryType.GLOBAL, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructWithNullTypeAndCorrectId() {
		new IdentifiedInvalidationEntry(null, "testId");
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructWithNullTypeAndNullId() {
		new IdentifiedInvalidationEntry(null, null);
	}

	@Test
	public void equalsWithSameInstance() {
		assertTrue("The equals() method should return true",
				identifiedInvalidationEntry.equals(identifiedInvalidationEntry));
	}

	@Test
	public void equalsWithDifferentClass() {
		assertFalse("The equals() method should return false",
				identifiedInvalidationEntry.equals(new Object()));
	}

	@Test
	public void equalsWithDifferentInstanceHavingSameTypeAndSameId() {

		final IdentifiedInvalidationEntry otherEntry = new IdentifiedInvalidationEntry(
				DefaultInvalidationEntryType.GLOBAL, "testId");

		assertTrue("The equals() method should return true",
				identifiedInvalidationEntry.equals(otherEntry));
	}

	@Test
	public void equalsWithDifferentInstanceHavingSameTypeAndDifferentId() {

		final IdentifiedInvalidationEntry otherEntry = new IdentifiedInvalidationEntry(
				DefaultInvalidationEntryType.GLOBAL, "otherTestId");

		assertFalse("The equals() method should return false",
				identifiedInvalidationEntry.equals(otherEntry));
	}

	@Test
	public void equalsWithDifferentInstanceHavingDifferentTypeAndSameId() {

		final IdentifiedInvalidationEntry otherEntry = new IdentifiedInvalidationEntry(
				new ClassInvalidationEntryType(Object.class), "testId");

		assertFalse("The equals() method should return false",
				identifiedInvalidationEntry.equals(otherEntry));
	}

	@Test
	public void equalsWithDifferentInstanceHavingDifferentTypeAndDifferentId() {

		final IdentifiedInvalidationEntry otherEntry = new IdentifiedInvalidationEntry(
				new ClassInvalidationEntryType(Object.class), "otherTestId");

		assertFalse("The equals() method should return false",
				identifiedInvalidationEntry.equals(otherEntry));
	}

	@Test
	public void hashCodeWithCorrectValue() {

		final int hashCode = identifiedInvalidationEntry.hashCode();

		assertTrue("Wrong hashCode", hashCode != 0);
	}

	@Test
	public void toStringWithCorrectValues() {

		final StringBuilder sbld = new StringBuilder();
		sbld.append("IdentifiedInvalidationEntry[type=")
				.append(DefaultInvalidationEntryType.GLOBAL);
		sbld.append(",id=").append("testId");
		sbld.append("]");

		final String toStringValue = identifiedInvalidationEntry.toString();

		assertNotNull("The toString() value shouldn't be null", toStringValue);
		assertEquals("Wrong toString() value", sbld.toString(), toStringValue);
	}

	@Test
	public void idGetterSetterWithCorrectValue() {

		final String newId = "newTestId";

		identifiedInvalidationEntry.setId(newId);

		final String id = identifiedInvalidationEntry.getId();

		assertNotNull("The ID shouldn't be null", id);
		assertEquals("Wrong ID", newId, id);
	}

	@Test(expected = IllegalArgumentException.class)
	public void idGetterSetterWithNullValue() {
		identifiedInvalidationEntry.setId(null);
	}

	@Test
	public void typeGetterSetterWithCorrectValue() {

		final InvalidationEntryType newType = new ClassInvalidationEntryType(getClass());

		identifiedInvalidationEntry.setType(newType);

		final InvalidationEntryType type = identifiedInvalidationEntry.getType();

		assertNotNull("The type shouldn't be null", type);
		assertEquals("Wrong type", newType, type);
	}

	@Test(expected = IllegalArgumentException.class)
	public void typeGetterSetterWithNullValue() {
		identifiedInvalidationEntry.setType(null);
	}

}
