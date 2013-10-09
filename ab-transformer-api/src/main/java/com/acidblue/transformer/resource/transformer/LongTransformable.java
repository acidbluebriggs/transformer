package com.acidblue.transformer.resource.transformer;

/**
 * Transforms a String into a Long.
 */
public class LongTransformable implements Transformable<Long, String> {

    /**
     * Transforms the given string into a Long
     * @param value String version of the Long to transform
     * @return A Long
     * @see Long#parseLong(String)
     */
    public Long transform(final String value) {

        return Long.parseLong(value);
    }
}
