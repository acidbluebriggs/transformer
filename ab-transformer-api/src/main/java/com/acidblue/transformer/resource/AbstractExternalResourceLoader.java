package com.acidblue.transformer.resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ResourceBundle;

/**
 * An external resource loader defines a resource loader that retrieves its resultant value from an external resource,
 * meaning not the actual value from a resource bundle. Binary files and such are stored as references within a resource
 * bundle (such as a file name like "my_image.png"). A resource loader needs to know how to retrieve the file which the
 * key represents.  These files may be stored within an external database, an http server, the local file system, an
 * eternal bean that contains a cache of items etc.
 *
 * @author <a href="mailto:acidblue@acidblue.com">Marc Miller (a.k.a. Briggs)</a>
 * @since 1.0
 */
public abstract class AbstractExternalResourceLoader<T> extends AbstractResourceLoader<T> {

    private final Logger LOG = LoggerFactory.getLogger(AbstractExternalResourceLoader.class);

    public AbstractExternalResourceLoader() {
    }

    protected AbstractExternalResourceLoader(final String resourceBundleBaseName, final String binding,
                                             final ResourceBundle.Control control) {
        super(resourceBundleBaseName, binding, control);
    }

    /**
     * Creates an abstract external resource loader which uses the given resource bundle name and the key which maps to
     * the underlying bundle's format for it's key.
     *
     * @param resourceBundleBaseName The base name of the resource bundle
     * @param colorKey               The suffix of the type that this loader represents
     */
    public AbstractExternalResourceLoader(final String resourceBundleBaseName, final String colorKey) {
        super(resourceBundleBaseName, colorKey);
    }

    /**
     * @inheritDoc
     */
    @Override
    public T loadResource(final String resourceName) {

        LOG.debug("Loading resource for name '{}'", resourceName);

        return StringUtils.isBlank(getResourceValue(resourceName)) ?
                null : loadExternalResource(getResourceValue(resourceName));
    }


    /**
     * This method defines how an external resource is loaded. Implementers define how to retrieve the value from the
     * given reference name.
     *
     * @param referenceName The name of the external resource
     * @return The object value for the given reference name
     */
    protected abstract T loadExternalResource(final String referenceName);
}


