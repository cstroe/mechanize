package com.gistlabs.mechanize.cache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.HttpContext;

import com.gistlabs.mechanize.filters.MechanizeChainFilter;
import com.gistlabs.mechanize.filters.MechanizeFilter;

/**
 * Support for cache and conditional HTTP request/response handling
 * 
 * Includes support for the following HTTP Headers to support caching: Cache-Control, Expires, Date, Age
 * Includes support for the following HTTP Headers to support conditions: Last-Modified, E-Tag, If-Modified-Since, If-None-Match
 *
 */
public class HttpCacheFilter implements MechanizeChainFilter {
	ConcurrentMap<String,CacheEntry> cache = new ConcurrentHashMap<String,CacheEntry>();

	@Override
	public HttpResponse execute(final HttpUriRequest request, final HttpContext context, final MechanizeFilter chain) {
		String uri = request.getURI().toString();

		// only handle GET requests
		if (! request.getMethod().equalsIgnoreCase("GET")) {
			maybeInvalidate(uri, request);
			return chain.execute(request, context);
		}

		CacheEntry previous = cache.get(uri);
		if (previous!=null && previous.isValid())
			return previous.response;

		if (previous!=null)
			previous.prepareConditionalGet(request);

		HttpResponse response = chain.execute(request, context); // call the chain

		if (response.getStatusLine().getStatusCode()==304) // not modified
			return previous.updateCacheValues(response).response;

		CacheEntry maybe = new CacheEntry(request, response);

		if (maybe.isCacheable())
			store(uri, previous, maybe);

		return response;
	}

	protected void maybeInvalidate(final String uri, final HttpUriRequest request) {
		// no-op right now, need to read some specs...
	}

	protected void store(final String uri, final CacheEntry cachedValue, final CacheEntry maybe) {
		if (cachedValue==null)
			cache.putIfAbsent(uri, maybe);
		else
			cache.replace(uri, cachedValue, maybe);
	}

}
