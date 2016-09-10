package com.github.ppodgorsek.juncacher.manager.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.junit.Before;
import org.junit.Test;

import com.github.ppodgorsek.juncacher.processor.InvalidationProcessor;

/**
 * Tests for the {@link ProcessorInvalidationManager} class.
 *
 * @author Paul Podgorsek
 */
public class ProcessorInvalidationManagerTest {

	private ProcessorInvalidationManager processorInvalidationManager;

	@Mock
	private InvalidationProcessor invalidationProcessor;

	@Before
	public void setUp() {

		EasyMockSupport.injectMocks(this);

		processorInvalidationManager = new ProcessorInvalidationManager();
	}

	@Test
	public void invalidationHelperGetterSetter() {

		assertNull("The invalidation processor shouldn't be set",
				processorInvalidationManager.getInvalidationProcessor());

		processorInvalidationManager.setInvalidationProcessor(invalidationProcessor);

		final InvalidationProcessor processor = processorInvalidationManager
				.getInvalidationProcessor();

		assertNotNull("The invalidation processor should be set", processor);
		assertEquals("Wrong invalidation processor", invalidationProcessor, processor);
	}

	@Test
	public void processEntries() {

		invalidationProcessor.invalidateEntries();
		EasyMock.expectLastCall();

		EasyMock.replay(invalidationProcessor);

		processorInvalidationManager.setInvalidationProcessor(invalidationProcessor);
		processorInvalidationManager.processEntries();

		EasyMock.verify(invalidationProcessor);
	}

}
