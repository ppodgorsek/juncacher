package org.ppodgorsek.cache.ehcache;

import java.util.List;

import org.ppodgorsek.cache.invalidation.helper.InvalidationHelper;
import org.ppodgorsek.cache.invalidation.model.impl.IdentifiedInvalidationEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;

/**
 * Helper used to evict entries from Ehcache.
 * 
 * @author Paul Podgorsek
 */
public class IdentifiedEhcacheHelper extends SimpleEhcacheHelper<IdentifiedInvalidationEntry> implements
		InvalidationHelper<IdentifiedInvalidationEntry> {

	private static final Logger LOGGER = LoggerFactory.getLogger(IdentifiedEhcacheHelper.class);

	/**
	 * Default constructor.
	 */
	public IdentifiedEhcacheHelper() {
		super();
	}

	@Override
	protected void evict(final IdentifiedInvalidationEntry entry, final List<String> cacheNames) {

		for (String cacheName : cacheNames) {

			Cache cache = getEhcacheCacheManager().getCache(cacheName);

			if (cache != null) {
				cache.evict(entry.getId());
				LOGGER.debug("Evicted entry '{}' from {} cache", entry.getId(), cacheName);
			}
		}
	}

}
