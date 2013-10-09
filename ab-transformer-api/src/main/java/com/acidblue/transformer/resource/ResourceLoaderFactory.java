package com.acidblue.transformer.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * A factory that will handle the retrieval of resource loaders based on the resource loaders name.
 *
 * @author <a href="mailto:acidblue@acidblue.com">Marc Miller (a.k.a. Briggs)</a>
 * @since 1.0
 */
public class ResourceLoaderFactory {

    private final Logger LOG = LoggerFactory.getLogger(ResourceLoaderFactory.class);
    private final Map<String, ResourceLoader<?>> resourceLoaders =
            new HashMap<String, ResourceLoader<?>>();


    /**
     * Returns the given resource loader by it's name
     *
     * @param loaderName The name of the resource loader
     * @return A resource loader for the given name
     * @throws NullPointerException     the given loader name was null
     * @throws IllegalArgumentException if no resource loaders are registered under the given loader name
     */
    @SuppressWarnings("unchecked")
    // We know we stored the correct type
    public <T> ResourceLoader<T> getResourceLoader(final String loaderName) {

        if (loaderName == null) {
            throw new NullPointerException("loaderName was null");
        }

        LOG.debug("Requesting loader named {}", loaderName);

        if (!resourceLoaders.containsKey(loaderName)) {
            throw new IllegalArgumentException(
                    String.format("no resource loaders for type '%s' are registered'", loaderName));
        }

        return (ResourceLoader<T>) resourceLoaders.get(loaderName);
    }


    /**
     * Sets the resource loaders available to the factory. This instnace of the factory will not maintain a reference to
     * the map passed, but only to the loaders themselves. Calling this method will also remove all previously set
     * loaders and replace them with the given map.
     *
     * @param loaders A map of loaders
     */
    public void setResourceLoaders(final Map<String, ResourceLoader<?>> loaders) {
        if (loaders == null) {
            throw new NullPointerException("loaders was null");
        }
        this.resourceLoaders.clear();
        this.resourceLoaders.putAll(loaders);
    }

}
