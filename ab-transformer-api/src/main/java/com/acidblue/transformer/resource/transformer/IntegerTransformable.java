package com.acidblue.transformer.resource.transformer;

/**
 * Transforms a string representation of a number into a Float.
 *
 * @see Float#parseFloat(String)
 */
public class IntegerTransformable implements Transformable<Integer, String> {

    /**
     * Transforms the given value into an Integer
     *
     * @param value The  value of the item (V) to be transformed
     * @return A Double instance of the given value
     * @see Integer#parseInt(String)
     */
    public Integer transform(final String value) {
        return Integer.parseInt(value);
    }

}
