package com.github.ppodgorsek.juncacher.varnish.strategy.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import com.github.ppodgorsek.juncacher.model.impl.DefaultInvalidationEntryType;
import com.github.ppodgorsek.juncacher.model.impl.IdentifiedInvalidationEntry;
import com.github.ppodgorsek.juncacher.varnish.strategy.impl.IdentifiedVarnishUrlStrategy;

/**
 * Tests for the {@link IdentifiedVarnishUrlStrategy} class.
 *
 * @author Paul Podgorsek
 */
public class IdentifiedVarnishUrlStrategyTest {

	private static final String ENTRY_ID = "entryId";

	private static final String URL_WITH_ENTRY_ID = "http://www.juncacher.com/ENTRY_ID";
	private static final String URL_WITHOUT_ENTRY_ID = "http://www.juncacher.com/";
	private static final String URL_REPLACED_WITH_ENTRY_ID = "http://www.juncacher.com/entryId";

	private IdentifiedVarnishUrlStrategy strategy;

	private IdentifiedInvalidationEntry entryWithId;

	@Before
	public void setUp() {

		strategy = new IdentifiedVarnishUrlStrategy();

		entryWithId = new IdentifiedInvalidationEntry(DefaultInvalidationEntryType.GLOBAL,
				ENTRY_ID);
	}

	@Test
	public void getUpdatedUrlWithCorrectEntryAndCorrectUrl() {

		final String updatedUrl = strategy.getUpdatedUrl(entryWithId, URL_WITH_ENTRY_ID);

		assertNotNull("The updated URL is required", updatedUrl);
		assertEquals("Wrong updated URL", URL_REPLACED_WITH_ENTRY_ID, updatedUrl);
	}

	@Test
	public void getUpdatedUrlWithCorrectEntryAndWrongUrl() {

		final String updatedUrl = strategy.getUpdatedUrl(entryWithId, URL_WITHOUT_ENTRY_ID);

		assertNotNull("The updated URL is required", updatedUrl);
		assertEquals("Wrong updated URL", URL_WITHOUT_ENTRY_ID, updatedUrl);
	}

	@Test(expected = NullPointerException.class)
	public void getUpdatedUrlWithCorrectEntryAndNullUrl() {
		strategy.getUpdatedUrl(entryWithId, null);
	}

	@Test(expected = NullPointerException.class)
	public void getUpdatedUrlWithNullEntryAndCorrectUrl() {
		strategy.getUpdatedUrl(null, URL_WITH_ENTRY_ID);
	}

	@Test(expected = NullPointerException.class)
	public void getUpdatedUrlWithNullEntryAndNullUrl() {
		strategy.getUpdatedUrl(null, null);
	}

	@Test
	public void placeholderGetterSetterWithDefaultValue() {

		assertEquals("Wrong placeholder", "ENTRY_ID", strategy.getPlaceholder());
	}

	@Test
	public void placeholderGetterSetterWithCorrectValue() {

		assertEquals("Wrong placeholder", "ENTRY_ID", strategy.getPlaceholder());

		strategy.setPlaceholder("NEW_ENTRY_ID");

		assertEquals("Wrong placeholder", "NEW_ENTRY_ID", strategy.getPlaceholder());
	}

	@Test(expected = IllegalArgumentException.class)
	public void placeholderGetterSetterWithNullValue() {
		strategy.setPlaceholder(null);
	}

}
