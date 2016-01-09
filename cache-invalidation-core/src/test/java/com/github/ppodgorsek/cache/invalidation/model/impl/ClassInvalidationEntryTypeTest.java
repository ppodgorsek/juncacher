package com.github.ppodgorsek.cache.invalidation.model.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests for the {@link ClassInvalidationEntryType} class.
 *
 * @author Paul Podgorsek
 */
public class ClassInvalidationEntryTypeTest {

	private final ClassInvalidationEntryType classInvalidationEntryType = new ClassInvalidationEntryType(
			Object.class);

	@Test(expected = IllegalArgumentException.class)
	public void constructWithNullClass() {
		new ClassInvalidationEntryType(null);
	}

	@Test
	public void equalsWithSameInstance() {
		assertTrue("The equals method should return true",
				classInvalidationEntryType.equals(classInvalidationEntryType));
	}

	@Test
	public void equalsWithDifferentInstanceHavingSameClass() {
		assertTrue("The equals method should return true",
				classInvalidationEntryType.equals(new ClassInvalidationEntryType(Object.class)));
	}

	@Test
	public void equalsWithDifferentInstanceHavingDifferentClass() {
		assertFalse("The equals method should return false",
				classInvalidationEntryType.equals(new ClassInvalidationEntryType(
						ClassInvalidationEntryType.class)));
	}

	@Test
	public void getValueWithCorrectClass() {

		final String value = classInvalidationEntryType.getValue();

		assertNotNull("The value shouldn't be null", value);
		assertEquals("Wrong value", Object.class.getSimpleName(), value);
	}

	@Test
	public void hashCodeWithCorrectValue() {

		final int hashCode = classInvalidationEntryType.hashCode();

		assertTrue("Wrong hashCode", hashCode != 0);
	}

	@Test
	public void toStringWithCorrectClass() {

		final String toStringValue = classInvalidationEntryType.toString();

		assertNotNull("The toString() value shouldn't be null", toStringValue);
		assertEquals("Wrong toString() value", Object.class.getSimpleName(), toStringValue);
	}

}
