package com.acidblue.transformer.resource.transformer;

import org.apache.commons.lang3.StringUtils;

import java.awt.Color;

/**
 * Transforms a String, which represents a color (as a 24-bit number) in either
 * an octal or hexadecimal form, by parsing the string value of the color.
 * If the resultant color is not able to be parsed then the color cyan is used to
 * aid in the visual aid in debugging that the color was not set.
 *
 *  @see java.awt.Color#decode(String)
 */
public class ColorTransformable implements Transformable<Color, String> {

    /**
     * @see java.awt.Color#decode(String)
     */
    public Color transform(final String value) {
        return StringUtils.isBlank(value) ? Color.cyan : Color.decode(value);
    }
}
