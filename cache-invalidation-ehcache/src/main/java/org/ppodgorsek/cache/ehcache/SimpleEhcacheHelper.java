package org.ppodgorsek.cache.ehcache;

import java.util.List;
import java.util.Map;

import org.ppodgorsek.cache.invalidation.exception.InvalidationException;
import org.ppodgorsek.cache.invalidation.helper.InvalidationHelper;
import org.ppodgorsek.cache.invalidation.model.InvalidationEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.cache.Cache;
import org.springframework.cache.ehcache.EhCacheCacheManager;

/**
 * Helper used to evict entries from Ehcache.
 * 
 * @author Paul Podgorsek
 */
public class SimpleEhcacheHelper<T extends InvalidationEntry> implements InvalidationHelper<T> {

	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleEhcacheHelper.class);

	private EhCacheCacheManager ehcacheCacheManager;

	private Map<Class<? extends T>, List<String>> cacheNameHolders;

	/**
	 * Default constructor.
	 */
	public SimpleEhcacheHelper() {
		super();
	}

	@Override
	public void invalidateEntry(final T entry) throws InvalidationException {

		List<String> cacheNameHolder = cacheNameHolders.get(entry.getClass());

		if (cacheNameHolder != null) {
			evict(entry, cacheNameHolder);
		}
	}

	/**
	 * Clears the cache associated with an invalidation entry.
	 * 
	 * @param entry
	 *            The entry that must be invalidated.
	 * @param cacheNames
	 *            The names of the caches that must be cleared according to the provided entry.
	 */
	protected void evict(final T entry, final List<String> cacheNames) {

		for (String cacheName : cacheNames) {

			Cache cache = ehcacheCacheManager.getCache(cacheName);

			if (cache != null) {
				cache.clear();
				LOGGER.debug("Cache {} has just been cleared", cacheName);
			}
		}
	}

	/**
	 * @return The ehcache cache manager.
	 */
	public EhCacheCacheManager getEhcacheCacheManager() {
		return ehcacheCacheManager;
	}

	/**
	 * @param manager
	 *            The new ehcache cache manager.
	 */
	@Required
	public void setEhcacheCacheManager(final EhCacheCacheManager manager) {
		ehcacheCacheManager = manager;
	}

	/**
	 * @return The cache name holders.
	 */
	public Map<Class<? extends T>, List<String>> getCacheNameHolders() {
		return cacheNameHolders;
	}

	/**
	 * @param holders
	 *            The new cache name holders.
	 */
	@Required
	public void setCacheNameHolders(final Map<Class<? extends T>, List<String>> holders) {
		cacheNameHolders = holders;
	}

}
