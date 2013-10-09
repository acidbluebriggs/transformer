package com.acidblue.transformer.resource;

import javax.swing.Icon;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;


/**
 * This class implements an icon that fills its background with the given color and is outlined by a black border.
 */
public class ColorIcon implements Icon {
    protected int width;
    protected int height;
    protected Color color;

    public ColorIcon(Color color) {
        this(color, 20, 20);
    }

    public ColorIcon(Color color, int width, int height) {
        this.color = color;
        this.width = width;
        this.height = height;
    }

    @Override
    public void paintIcon(Component c, Graphics graphics, int x, int y) {
        graphics.setColor(color);
        graphics.fillRect(x, y, getIconWidth(), getIconHeight());
        graphics.setColor(Color.BLACK);
        graphics.drawRect(x, y, getIconWidth() - 1, getIconHeight() - 1);
    }

    @Override
    public int getIconWidth() {
        return width;
    }

    @Override
    public int getIconHeight() {
        return height;
    }
}
