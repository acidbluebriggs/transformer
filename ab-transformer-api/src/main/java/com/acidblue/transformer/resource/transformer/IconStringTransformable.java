package com.acidblue.transformer.resource.transformer;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * Transforms the name of the given image into an icon.
 */
public class IconStringTransformable implements Transformable<Icon, String> {

    /**
     * Returns an icon from the given resource name.
     *
     * @param value The file name of the image
     * @return An icon
     * @see javax.swing.ImageIcon
     */
    public Icon transform(final String value) {

        return value == null ? null :
                new ImageIcon(getClass().getClassLoader().getResource(value));

    }
}
