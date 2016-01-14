package com.github.ppodgorsek.juncacher.model.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for the {@link DefaultInvalidationEntryType} class.
 *
 * @author Paul Podgorsek
 */
public class DefaultInvalidationEntryTypeTest {

	@Test
	public void getValue() {
		assertEquals("Wrong value", "GLOBAL", DefaultInvalidationEntryType.GLOBAL.getValue());
	}

}
