package com.acidblue.transformer.resource.transformer;


/**
 * A transformable which simply returns the string which was passed. This allows for symmetry by treating
 * string as all other primitive types within the system.
 */
public class StringTransformable implements Transformable<String, String> {


    /**
     * Returns the string passed.
     *
     * @param value A string
     * @return The initial string
     */
    public String transform(final String value) {
        return value;
    }
}
