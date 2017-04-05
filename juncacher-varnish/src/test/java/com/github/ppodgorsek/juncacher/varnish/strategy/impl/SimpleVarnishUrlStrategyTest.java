package com.github.ppodgorsek.juncacher.varnish.strategy.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.github.ppodgorsek.juncacher.model.impl.DefaultInvalidationEntryType;
import com.github.ppodgorsek.juncacher.model.impl.TypedInvalidationEntry;

/**
 * Tests for the {@link SimpleVarnishUrlStrategy} class.
 *
 * @author Paul Podgorsek
 */
public class SimpleVarnishUrlStrategyTest {

	private static final String URL = "http://www.juncacher.com/";

	private SimpleVarnishUrlStrategy strategy;

	private TypedInvalidationEntry entry;

	@Before
	public void setUp() {

		strategy = new SimpleVarnishUrlStrategy();

		entry = new TypedInvalidationEntry(DefaultInvalidationEntryType.GLOBAL);
	}

	@Test
	public void getUpdatedUrlWithCorrectEntryAndCorrectUrl() {

		final String updatedUrl = strategy.getUpdatedUrl(entry, URL);

		assertNotNull("The updated URL is required", updatedUrl);
		assertEquals("Wrong updated URL", URL, updatedUrl);
	}

	@Test
	public void getUpdatedUrlWithCorrectEntryAndNullUrl() {

		final String updatedUrl = strategy.getUpdatedUrl(entry, null);

		assertNull("The updated URL should be null", updatedUrl);
	}

	@Test
	public void getUpdatedUrlWithNullEntryAndCorrectUrl() {

		final String updatedUrl = strategy.getUpdatedUrl(null, URL);

		assertNotNull("The updated URL is required", updatedUrl);
		assertEquals("Wrong updated URL", URL, updatedUrl);
	}

	@Test
	public void getUpdatedUrlWithNullEntryAndNullUrl() {

		final String updatedUrl = strategy.getUpdatedUrl(null, null);

		assertNull("The updated URL should be null", updatedUrl);
	}

}
