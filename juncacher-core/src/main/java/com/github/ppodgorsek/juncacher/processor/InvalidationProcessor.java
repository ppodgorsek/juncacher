package com.github.ppodgorsek.juncacher.processor;

import java.util.Collection;

import com.github.ppodgorsek.juncacher.model.InvalidationEntry;

/**
 * Processor that invalidates entries.
 *
 * @since 1.1
 * @author Paul Podgorsek
 */
public interface InvalidationProcessor {

	/**
	 * Invalidates cache entries.
	 *
	 * @param entries
	 *            The entries that must be invalidated.
	 * @return The entries that have been invalidated.
	 */
	Collection<InvalidationEntry> invalidateEntries(Collection<InvalidationEntry> entries);

}
