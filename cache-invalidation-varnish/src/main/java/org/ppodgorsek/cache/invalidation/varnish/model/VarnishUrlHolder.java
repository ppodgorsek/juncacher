package org.ppodgorsek.cache.invalidation.varnish.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;

/**
 * Holder containing the URLs for an invalidation entry type's BAN and PURGE requests.
 * 
 * @author Paul Podgorsek
 */
public class VarnishUrlHolder {

	private List<String> banUrls;

	private List<String> purgeUrls;

	/**
	 * Default constructor.
	 */
	public VarnishUrlHolder() {
		super();
	}

	public List<String> getBanUrls() {
		return banUrls;
	}

	@Required
	public void setBanUrls(final List<String> urls) {
		banUrls = urls;
	}

	public List<String> getPurgeUrls() {
		return purgeUrls;
	}

	@Required
	public void setPurgeUrls(final List<String> urls) {
		purgeUrls = urls;
	}

}
