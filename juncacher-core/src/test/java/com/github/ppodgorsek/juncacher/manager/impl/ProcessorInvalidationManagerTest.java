package com.github.ppodgorsek.juncacher.manager.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.ppodgorsek.juncacher.collector.InvalidationCollector;
import com.github.ppodgorsek.juncacher.model.InvalidationEntry;
import com.github.ppodgorsek.juncacher.model.impl.SimpleInvalidationEntryType;
import com.github.ppodgorsek.juncacher.model.impl.TypedInvalidationEntry;
import com.github.ppodgorsek.juncacher.processor.InvalidationProcessor;

/**
 * Tests for the {@link ProcessorInvalidationManager} class.
 *
 * @author Paul Podgorsek
 */
public class ProcessorInvalidationManagerTest {

	private ProcessorInvalidationManager processorInvalidationManager;

	@Mock
	private InvalidationCollector collector1;

	@Mock
	private InvalidationCollector collector2;

	@Mock
	private InvalidationProcessor processor1;

	@Mock
	private InvalidationProcessor processor2;

	private InvalidationEntry entry1;
	private InvalidationEntry entry2;
	private InvalidationEntry entry3;
	private Collection<InvalidationEntry> entries;

	@Before
	public void setUp() {

		EasyMockSupport.injectMocks(this);

		final Map<InvalidationProcessor, InvalidationCollector> processorsWithCollectors = new LinkedHashMap<>();
		processorsWithCollectors.put(processor1, collector1);
		processorsWithCollectors.put(processor2, collector2);

		processorInvalidationManager = new ProcessorInvalidationManager(processorsWithCollectors);

		entry1 = new TypedInvalidationEntry(new SimpleInvalidationEntryType("type1"));
		entry2 = new TypedInvalidationEntry(new SimpleInvalidationEntryType("type2"));
		entry3 = new TypedInvalidationEntry(new SimpleInvalidationEntryType("type3"));

		entries = new ArrayList<>();
		entries.add(entry1);
		entries.add(entry2);
		entries.add(entry3);
	}

	@After
	public void tearDown() {
		EasyMock.verify(collector1, collector2, processor1, processor2);
	}

	@Test
	public void constructWithCorrectMap() {

		EasyMock.replay(collector1, collector2, processor1, processor2);

		final Map<InvalidationProcessor, InvalidationCollector> processorsWithCollectors = new LinkedHashMap<>();
		processorsWithCollectors.put(processor1, collector1);
		processorsWithCollectors.put(processor2, collector2);

		processorInvalidationManager = new ProcessorInvalidationManager(processorsWithCollectors);
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructWithEmptyMap() {

		EasyMock.replay(collector1, collector2, processor1, processor2);

		new ProcessorInvalidationManager(
				new HashMap<InvalidationProcessor, InvalidationCollector>());
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructWithMapHavingNullKey() {

		EasyMock.replay(collector1, collector2, processor1, processor2);

		final Map<InvalidationProcessor, InvalidationCollector> processorsWithCollectors = new LinkedHashMap<>();
		processorsWithCollectors.put(null, collector1);

		processorInvalidationManager = new ProcessorInvalidationManager(processorsWithCollectors);
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructWithMapHavingNullValue() {

		EasyMock.replay(collector1, collector2, processor1, processor2);

		final Map<InvalidationProcessor, InvalidationCollector> processorsWithCollectors = new LinkedHashMap<>();
		processorsWithCollectors.put(processor1, null);

		processorInvalidationManager = new ProcessorInvalidationManager(processorsWithCollectors);
	}

	@Test
	public void addInvalidationEntries() {

		collector1.addInvalidationEntries(entries);
		EasyMock.expectLastCall();

		EasyMock.replay(collector1, collector2, processor1, processor2);

		processorInvalidationManager.addInvalidationEntries(entries);
	}

	@Test
	public void addInvalidationEntry() {

		collector1.addInvalidationEntry(entry1);
		EasyMock.expectLastCall();

		EasyMock.replay(collector1, collector2, processor1, processor2);

		processorInvalidationManager.addInvalidationEntry(entry1);
	}

	@Test
	public void getProcessorsWithCollectors() {

		EasyMock.replay(collector1, collector2, processor1, processor2);

		final Map<InvalidationProcessor, InvalidationCollector> processorsWithCollectors = new LinkedHashMap<>();
		processorsWithCollectors.put(processor1, collector1);
		processorsWithCollectors.put(processor2, collector2);

		processorInvalidationManager = new ProcessorInvalidationManager(processorsWithCollectors);

		final Map<InvalidationProcessor, InvalidationCollector> processorsWithCollectorsFetched = processorInvalidationManager
				.getProcessorsWithCollectors();

		assertNotNull("The processor with collector map shouldn't be null",
				processorsWithCollectorsFetched);
		assertNotSame(
				"The processor with collector map should be different from the one given to the constructor",
				processorsWithCollectors, processorsWithCollectorsFetched);
		assertEquals(
				"The contents of the processor with collector map should be equal to the one given to the constructor",
				processorsWithCollectors, processorsWithCollectorsFetched);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void getProcessorsWithCollectorsAndModifyResult() {

		EasyMock.replay(collector1, collector2, processor1, processor2);

		final Map<InvalidationProcessor, InvalidationCollector> processorsWithCollectorsFetched = processorInvalidationManager
				.getProcessorsWithCollectors();

		processorsWithCollectorsFetched.put(processor1, collector2);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void processEntries() {

		final List<InvalidationEntry> entriesFromCollector1 = new ArrayList<>();
		entriesFromCollector1.add(entry2);

		final List<InvalidationEntry> entriesFromCollector2 = new ArrayList<>();
		entriesFromCollector2.add(entry2);

		final List<InvalidationEntry> entriesEvicted = new ArrayList<>();
		entriesEvicted.add(entry2);

		collector1.addInvalidationEntries(EasyMock.anyObject(List.class));
		EasyMock.expectLastCall();

		EasyMock.expect(collector1.getEntries()).andReturn(entriesFromCollector1);
		EasyMock.expect(processor1.invalidateEntries(entriesFromCollector1))
				.andReturn(entriesFromCollector1);

		collector1.consume(entriesFromCollector1);
		EasyMock.expectLastCall();

		collector2.addInvalidationEntries(entriesFromCollector1);
		EasyMock.expectLastCall();

		EasyMock.expect(collector2.getEntries()).andReturn(entriesFromCollector2);
		EasyMock.expect(processor2.invalidateEntries(entriesFromCollector2))
				.andReturn(entriesEvicted);

		collector2.consume(entriesEvicted);
		EasyMock.expectLastCall();

		EasyMock.replay(collector1, collector2, processor1, processor2);

		processorInvalidationManager.processEntries();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void processEntriesImmediately() {

		final Collection<InvalidationEntry> entriesForProcessor1 = new ArrayList<>();
		entriesForProcessor1.add(entry2);
		entriesForProcessor1.add(entry1);
		entriesForProcessor1.add(entry3);

		final List<InvalidationEntry> entriesForProcessor2 = new ArrayList<>();
		entriesForProcessor2.add(entry2);
		entriesForProcessor2.add(entry1);

		EasyMock.expect(processor1.invalidateEntries(entriesForProcessor1))
				.andReturn(entriesForProcessor2);

		collector1.addInvalidationEntries(EasyMock.anyObject(List.class));
		EasyMock.expectLastCall();

		EasyMock.expect(processor2.invalidateEntries(entriesForProcessor2))
				.andReturn(entriesForProcessor2);

		EasyMock.replay(collector1, collector2, processor1, processor2);

		processorInvalidationManager.processEntries(entriesForProcessor1);
	}

	@Test
	public void processEntryImmediately() {

		final Collection<InvalidationEntry> entriesForProcessor1 = new ArrayList<>();
		entriesForProcessor1.add(entry2);

		final List<InvalidationEntry> entriesForProcessor2 = new ArrayList<>();
		entriesForProcessor2.add(entry2);

		EasyMock.expect(processor1.invalidateEntries(entriesForProcessor1))
				.andReturn(entriesForProcessor2);

		EasyMock.expect(processor2.invalidateEntries(entriesForProcessor2))
				.andReturn(entriesForProcessor2);

		EasyMock.replay(collector1, collector2, processor1, processor2);

		processorInvalidationManager.processEntry(entry2);
	}

}
