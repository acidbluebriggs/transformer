package com.acidblue.transformer.resource;

import java.io.IOException;
import java.io.StringReader;
import java.util.Enumeration;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Vector;

/**
 * A simple resource bundle that takes a string as it's input. The string spec is exactly the same as you would expect a
 * java property file to be formatted (key=value\n).
 *
 * @author <a href="mailto:acidblue@acidblue.com">Marc Miller (a.k.a. Briggs)</a>
 * @since 1.0
 */
public class StringResourceBundle extends ResourceBundle {

    private Properties properties;

    public StringResourceBundle(final String resource) throws IOException {
        this.properties = new Properties();
        this.properties.load(new StringReader(resource));
    }

    @Override
    protected Object handleGetObject(final String key) {
        return properties.getProperty(key);
    }

    @Override
    public Enumeration<String> getKeys() {
        final Vector<String> vString = new Vector<String>();

        for (final Enumeration en = properties.keys(); en.hasMoreElements();) {
            vString.add((String) en.nextElement());
        }

        return vString.elements();
    }
}
