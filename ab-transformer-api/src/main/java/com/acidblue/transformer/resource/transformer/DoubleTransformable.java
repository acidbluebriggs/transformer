package com.acidblue.transformer.resource.transformer;

/**
 * Transforms a string representation of a number into a Double.
 *
 * @see Double#parseDouble(String)
 */
public class DoubleTransformable implements Transformable<Double, String> {

    /**
     * Transforms the given value into a Double.
     *
     * @param value The value of the item (V) to be transformed
     * @return A Double instance of the given value
     * @see Double#parseDouble(String)
     */
    public Double transform(final String value) {
        return Double.parseDouble(value);
    }
}
