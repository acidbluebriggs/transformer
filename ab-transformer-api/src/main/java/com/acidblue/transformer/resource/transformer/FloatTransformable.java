package com.acidblue.transformer.resource.transformer;

/**
 * Transforms a string representation of a number into a Float.
 *
 * @see Float#parseFloat(String)
 */
class FloatTransformable implements Transformable<Float, String> {

    /**
     * Transforms the given value into a Float.
     *
     * @param value The  value of the item (V) to be decoded
     * @return A Double instance of the given value
     * @see Float#parseFloat(String)
     */
    public Float transform(final String value) {
        return Float.parseFloat(value);
    }
}
