package com.github.ppodgorsek.juncacher.processor.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.junit.Before;
import org.junit.Test;

import com.github.ppodgorsek.juncacher.helper.InvalidationHelper;

/**
 * Tests for the {@link HelperInvalidationProcessor} class.
 *
 * @author Paul Podgorsek
 */
public class HelperInvalidationProcessorTest {

	private HelperInvalidationProcessor helperInvalidationProcessor;

	@Mock
	private InvalidationHelper invalidationHelper;

	@Before
	public void setUp() {

		EasyMockSupport.injectMocks(this);

		helperInvalidationProcessor = new HelperInvalidationProcessor();
	}

	@Test
	public void invalidationHelperGetterSetter() {

		assertNull("The invalidation helper shouldn't be set",
				helperInvalidationProcessor.getInvalidationHelper());

		helperInvalidationProcessor.setInvalidationHelper(invalidationHelper);

		final InvalidationHelper helper = helperInvalidationProcessor.getInvalidationHelper();

		assertNotNull("The invalidation helper should be set", helper);
		assertEquals("Wrong invalidation helper", invalidationHelper, helper);
	}

	@Test
	public void processEntries() {

		invalidationHelper.invalidateEntries();
		EasyMock.expectLastCall();

		EasyMock.replay(invalidationHelper);

		helperInvalidationProcessor.setInvalidationHelper(invalidationHelper);

		helperInvalidationProcessor.processEntries();

		EasyMock.verify(invalidationHelper);
	}

}
