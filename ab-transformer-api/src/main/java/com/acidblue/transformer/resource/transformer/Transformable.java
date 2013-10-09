package com.acidblue.transformer.resource.transformer;


/**
 * Defines a mechanism for transforming a string, or any other arbitrary value into the type implemented by the
 * interface.
 *
 * @param <V> The value of the type to return
 */
public interface Transformable<V, K> {


    /**
     * This method will attempt to transform the given value and return this string as an instance of the given class
     * type.
     *
     * @param value The value of the item (V) to be transform
     * @return The transformed value
     */
    public V transform(final K value);
}
