package com.acidblue.transformer.resource.transformer;

/**
 * Transforms a string to a boolean. Accepts "true" or "false".  This instance delegates to {@link
 * Boolean#parseBoolean(String)}.
 *
 * @see Boolean#parseBoolean(String) for the full description on how a boolean is parsed
 */
public class BooleanTransformable implements Transformable<Boolean, String> {

    /**
     * Returns the boolean value of the given string.
     *
     * @param value A string
     * @return A boolean representation
     * @see Boolean#parseBoolean(String)
     */
    public Boolean transform(final String value) {
        return Boolean.parseBoolean(value);
    }
}
