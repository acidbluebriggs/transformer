package com.acidblue.transformer.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A class that acts as a cache/proxy for resources. Since resource may be loaded from remote locations, which creates
 * high latency and significantly increases network activity, a cache is needed.
 * <p/>
 * <p/>
 * This class offers a simple map-like interface that allows for the caching of items.
 *
 * @author <a href="mailto:acidblue@acidblue.com">Marc Miller (a.k.a. Briggs)</a>
 * @since 1.0
 */
public class ResourceCache<String, T> {

    private final Logger LOG = LoggerFactory.getLogger(ResourceCache.class);
    private final Map<String, T> cachedResources = new ConcurrentHashMap<String, T>();

    public int size() {
        return cachedResources.size();
    }

    public T get(final String key) {
        return cachedResources.get(key);
    }

    public void cache(final String key, final T value) {
        this.cachedResources.put(key, value);
    }

    public void uncache(final String key) {
        this.cachedResources.remove(key);
    }

    public void clearCache() {
        cachedResources.clear();
    }

}
