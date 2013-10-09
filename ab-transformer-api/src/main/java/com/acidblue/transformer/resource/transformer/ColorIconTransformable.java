package com.acidblue.transformer.resource.transformer;

import com.acidblue.transformer.resource.ColorIcon;

import java.awt.Color;


/**
 * Transforms the given string, which represents a color (as a 24-bit number) in either
 * an octal or hexadecimal form, into an ColorIcon.
 *
 * @see java.awt.Color#decode(String)
 * @see com.acidblue.transformer.resource.ColorIcon
 * @author <a href="mailto:acidblue@acidblue.com">Marc Miller (a.k.a. Briggs)</a>
 * @since 1.0
 */
public class ColorIconTransformable implements Transformable<ColorIcon, String> {

    /**
     * Transforms the given string value into a new color icon.
     *
     * @param value The value of the item (V) to be transformed.
     * @return An instance of ColorIcon
     * @see Color#decode(String) for more information on the exepected input format.
     */
    public ColorIcon transform(final String value) {
        return new ColorIcon(Color.decode(value));
    }
}
