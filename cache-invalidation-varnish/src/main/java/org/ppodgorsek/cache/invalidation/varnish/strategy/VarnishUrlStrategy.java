package org.ppodgorsek.cache.invalidation.varnish.strategy;

import java.util.List;

import org.apache.commons.httpclient.methods.GetMethod;
import org.ppodgorsek.cache.invalidation.model.InvalidationEntry;
import org.ppodgorsek.cache.invalidation.varnish.http.HttpBanMethod;
import org.ppodgorsek.cache.invalidation.varnish.http.HttpPurgeMethod;

/**
 * @author Paul Podgorsek
 */
public interface VarnishUrlStrategy<T extends InvalidationEntry> {

	List<HttpBanMethod> getBanMethods(T entry);

	List<GetMethod> getGetMethods(T entry);

	List<HttpPurgeMethod> getPurgeMethods(T entry);

}
