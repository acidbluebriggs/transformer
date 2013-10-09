package com.acidblue.transformer.resource;

/**
 * Loads a resource.
 *
 * @author <a href="mailto:acidblue@acidblue.com">Marc Miller (a.k.a. Briggs)</a>
 * @since 1.0
 */
public interface ResourceFactory<K> {

    /**
     * Returns a resource (text, images etc) by passing in the resources name and type. Any type of resource can be
     * configured.
     *
     * @param resourceName THe name of the resource to look up
     * @param type         The type of the resource
     * @param <T>          The expected return type
     * @return The value of the resource
     * @throws IllegalArgumentException if the requested type is not registered as a valid resource
     * @throws java.util.MissingResourceException
     *                                  if there is no resource defined for the given key
     */
    <T> T value(K resourceName, String type);

    boolean exists(K resourceName, String type);
}
