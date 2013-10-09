package com.acidblue.transformer.resource.loader;

import com.acidblue.transformer.resource.AbstractResourceLoader;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ResourceBundle;

/**
 * Encapsulates access to a resource bundle and allows simple keyed access.
 *
 * @author <a href="mailto:acidblue@acidblue.com">Marc Miller (a.k.a. Briggs)</a>
 * @since 1.0
 */
public class StringLoader extends AbstractResourceLoader<String> {

    private final Logger LOG = LoggerFactory.getLogger(StringLoader.class);

    public StringLoader() {}

    public StringLoader(final String resourceBundleBaseName, final String binding,
                        final ResourceBundle.Control control) {
        super(resourceBundleBaseName, binding, control);
    }

    public StringLoader(final String resourceBundleBaseName, final String stringKey) {
        super(resourceBundleBaseName, stringKey);
    }

    @Override
    public String loadResource(final String resourceName) {

        final String resourceValue = StringUtils.isBlank(getResourceValue(resourceName)) ?
                null : getResourceValue(resourceName);

        LOG.debug("Loaded resource {} value as {}", resourceName, resourceValue);

        return resourceValue; 
    }
}
