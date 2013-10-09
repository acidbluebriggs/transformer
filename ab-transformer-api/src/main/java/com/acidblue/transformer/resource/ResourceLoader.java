package com.acidblue.transformer.resource;

/**
 * An abstraction of a resource bundle.  This interface allows for the loading of any arbitrary type from a resource
 * bundle.
 *
 * @author <a href="mailto:acidblue@acidblue.com">Marc Miller (a.k.a. Briggs)</a>
 * @since 1.0
 */
public interface ResourceLoader<T> {

    /**
     * Returns a resource by passing name.
     *
     * @param resourceName The name of the resource
     * @return The value of the resource
     */
    T get(String resourceName);

    boolean hasResource(String resourceName);
}