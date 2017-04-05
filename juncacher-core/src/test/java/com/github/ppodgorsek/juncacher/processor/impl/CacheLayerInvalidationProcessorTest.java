package com.github.ppodgorsek.juncacher.processor.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.ppodgorsek.juncacher.exception.InvalidationException;
import com.github.ppodgorsek.juncacher.interceptor.InvalidationInterceptor;
import com.github.ppodgorsek.juncacher.model.InvalidationEntry;
import com.github.ppodgorsek.juncacher.model.impl.SimpleInvalidationEntryType;
import com.github.ppodgorsek.juncacher.model.impl.TypedInvalidationEntry;
import com.github.ppodgorsek.juncacher.strategy.InvalidationStrategy;

/**
 * Tests for the {@link CacheLayerInvalidationProcessor} class.
 *
 * @author Paul Podgorsek
 */
public class CacheLayerInvalidationProcessorTest {

	private static final String INVALIDATION_TYPE_1 = "type1";
	private static final String INVALIDATION_TYPE_2 = "type2";
	private static final String INVALIDATION_TYPE_3 = "type3";

	private CacheLayerInvalidationProcessor cacheLayerInvalidationProcessor;

	@Mock
	private InvalidationInterceptor interceptor1;

	@Mock
	private InvalidationInterceptor interceptor2;

	@Mock
	private InvalidationStrategy<InvalidationEntry> strategy1;

	@Mock
	private InvalidationStrategy<InvalidationEntry> strategy2;

	private List<InvalidationInterceptor> interceptors;

	private Map<String, InvalidationStrategy<InvalidationEntry>> strategies;

	private InvalidationEntry entry1;
	private InvalidationEntry entry2;
	private InvalidationEntry entry3;
	private Collection<InvalidationEntry> entries;

	@Before
	public void setUp() {

		EasyMockSupport.injectMocks(this);

		interceptors = new ArrayList<>();
		interceptors.add(interceptor1);
		interceptors.add(interceptor2);

		strategies = new HashMap<>();
		strategies.put(INVALIDATION_TYPE_1, strategy1);
		strategies.put(INVALIDATION_TYPE_2, strategy2);

		cacheLayerInvalidationProcessor = new CacheLayerInvalidationProcessor();
		cacheLayerInvalidationProcessor.setInterceptors(interceptors);
		cacheLayerInvalidationProcessor.setStrategies(strategies);

		entry1 = new TypedInvalidationEntry(new SimpleInvalidationEntryType(INVALIDATION_TYPE_1));
		entry2 = new TypedInvalidationEntry(new SimpleInvalidationEntryType(INVALIDATION_TYPE_2));
		entry3 = new TypedInvalidationEntry(new SimpleInvalidationEntryType(INVALIDATION_TYPE_3));
	}

	@After
	public void tearDown() {
		EasyMock.verify(interceptor1, interceptor2);
		EasyMock.verify(strategy1, strategy2);
	}

	@Test
	public void invalidateEntriesWithCorrectList() throws Exception {

		entries = new ArrayList<>();
		entries.add(entry1);
		entries.add(entry2);

		interceptor1.preHandle();
		EasyMock.expectLastCall();

		interceptor2.preHandle();
		EasyMock.expectLastCall();

		EasyMock.expect(strategy1.canHandle(entry1)).andReturn(true);
		strategy1.invalidate(entry1);
		EasyMock.expectLastCall();

		EasyMock.expect(strategy2.canHandle(entry2)).andReturn(true);
		strategy2.invalidate(entry2);
		EasyMock.expectLastCall();

		interceptor1.postHandle();
		EasyMock.expectLastCall();

		interceptor2.postHandle();
		EasyMock.expectLastCall();

		EasyMock.replay(interceptor1, interceptor2);
		EasyMock.replay(strategy1, strategy2);

		final Collection<InvalidationEntry> invalidatedEntries = cacheLayerInvalidationProcessor
				.invalidateEntries(entries);

		assertNotNull("The list of invalidated entries shouldn't be null", invalidatedEntries);
		assertEquals("Wrong number of invalidated entries", 2, invalidatedEntries.size());
	}

	@Test
	public void invalidateEntriesWithCorrectListButPreHandleFailing() throws Exception {

		entries = new ArrayList<>();
		entries.add(entry1);
		entries.add(entry2);

		interceptor1.preHandle();
		EasyMock.expectLastCall();

		interceptor2.preHandle();
		EasyMock.expectLastCall().andThrow(new InvalidationException());

		EasyMock.replay(interceptor1, interceptor2);
		EasyMock.replay(strategy1, strategy2);

		final Collection<InvalidationEntry> invalidatedEntries = cacheLayerInvalidationProcessor
				.invalidateEntries(entries);

		assertNotNull("The list of invalidated entries shouldn't be null", invalidatedEntries);
		assertTrue("There should be no invalidated entries", invalidatedEntries.isEmpty());
	}

	@Test
	public void invalidateEntriesWithCorrectListButPostHandleFailing() throws Exception {

		entries = new ArrayList<>();
		entries.add(entry1);
		entries.add(entry2);

		interceptor1.preHandle();
		EasyMock.expectLastCall();

		interceptor2.preHandle();
		EasyMock.expectLastCall();

		EasyMock.expect(strategy1.canHandle(entry1)).andReturn(true);
		strategy1.invalidate(entry1);
		EasyMock.expectLastCall();

		EasyMock.expect(strategy2.canHandle(entry2)).andReturn(true);
		strategy2.invalidate(entry2);
		EasyMock.expectLastCall();

		interceptor1.postHandle();
		EasyMock.expectLastCall().andThrow(new InvalidationException());

		EasyMock.replay(interceptor1, interceptor2);
		EasyMock.replay(strategy1, strategy2);

		final Collection<InvalidationEntry> invalidatedEntries = cacheLayerInvalidationProcessor
				.invalidateEntries(entries);

		assertNotNull("The list of invalidated entries shouldn't be null", invalidatedEntries);
		assertEquals("Wrong number of invalidated entries", 2, invalidatedEntries.size());
	}

	@Test(expected = IllegalArgumentException.class)
	public void invalidateEntryWithNullEntry() {

		EasyMock.replay(interceptor1, interceptor2);
		EasyMock.replay(strategy1, strategy2);

		cacheLayerInvalidationProcessor.invalidateEntry(null);
	}

	@Test
	public void invalidateEntryWithCorrectEntryHavingNoStrategy() {

		EasyMock.replay(interceptor1, interceptor2);
		EasyMock.replay(strategy1, strategy2);

		final InvalidationEntry invalidatedEntry = cacheLayerInvalidationProcessor
				.invalidateEntry(entry3);

		assertNotNull("The invalidated entry shouldn't be null", invalidatedEntry);
		assertEquals("Wrong invalidated entry", entry3, invalidatedEntry);
	}

	@Test
	public void invalidateEntryWithCorrectEntryHavingStrategyNotHandlingIt() {

		EasyMock.expect(strategy1.canHandle(entry1)).andReturn(false);

		EasyMock.replay(interceptor1, interceptor2);
		EasyMock.replay(strategy1, strategy2);

		final InvalidationEntry invalidatedEntry = cacheLayerInvalidationProcessor
				.invalidateEntry(entry1);

		assertNotNull("The invalidated entry shouldn't be null", invalidatedEntry);
		assertEquals("Wrong invalidated entry", entry1, invalidatedEntry);
	}

	@Test
	public void invalidateEntryWithCorrectEntryHavingStrategyThrowingException() throws Exception {

		EasyMock.expect(strategy1.canHandle(entry1)).andReturn(true);
		strategy1.invalidate(entry1);
		EasyMock.expectLastCall().andThrow(new InvalidationException());

		EasyMock.replay(interceptor1, interceptor2);
		EasyMock.replay(strategy1, strategy2);

		final InvalidationEntry invalidatedEntry = cacheLayerInvalidationProcessor
				.invalidateEntry(entry1);

		assertNull("No entry should be invalidated when the invalidation fails", invalidatedEntry);
	}

	@Test
	public void invalidateEntryWithCorrectEntryHavingCorrectStrategy() throws Exception {

		EasyMock.expect(strategy2.canHandle(entry2)).andReturn(true);
		strategy2.invalidate(entry2);
		EasyMock.expectLastCall();

		EasyMock.replay(interceptor1, interceptor2);
		EasyMock.replay(strategy1, strategy2);

		final InvalidationEntry invalidatedEntry = cacheLayerInvalidationProcessor
				.invalidateEntry(entry2);

		assertNotNull("The invalidated entry shouldn't be null", invalidatedEntry);
		assertEquals("Wrong invalidated entry", entry2, invalidatedEntry);
	}

	@Test
	public void interceptorsGetterSetter() {

		cacheLayerInvalidationProcessor.setInterceptors(interceptors);

		final List<InvalidationInterceptor> interceptorsFetched = cacheLayerInvalidationProcessor
				.getInterceptors();

		assertNotNull("The interceptors shouldn't be null", interceptorsFetched);
		assertEquals("Wrong list of interceptors", interceptors, interceptorsFetched);
	}

	@Test
	public void strategiesGetterSetter() {

		cacheLayerInvalidationProcessor.setStrategies(strategies);

		final Map<String, InvalidationStrategy<InvalidationEntry>> strategiesFetched = cacheLayerInvalidationProcessor
				.getStrategies();

		assertNotNull("The strategies shouldn't be null", strategiesFetched);
		assertEquals("Wrong map of strategies", interceptors, strategiesFetched);
	}

}
