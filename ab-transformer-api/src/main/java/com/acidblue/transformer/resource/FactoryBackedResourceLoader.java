package com.acidblue.transformer.resource;

/**
 * An implementation of the resource loader interface that uses a resource loader factory as it's entry point to the
 * resources.
 *
 * @author <a href="mailto:acidblue@acidblue.com">Marc Miller (a.k.a. Briggs)</a>
 * @since 1.0
 */
public class FactoryBackedResourceLoader implements ResourceFactory<String> {

    private ResourceLoaderFactory factory;

    public boolean exists(final String resourceName, final String type) {
        return factory.getResourceLoader(type).hasResource(resourceName);
    }

    /**
     * @inheritDoc
     */
    public <T> T value(final String resourceName, final String type) {

        final ResourceLoader<T> loader = factory.getResourceLoader(type);

        return loader.get(resourceName);
    }

    /**
     * Sets the instance of the Resource Loader Factory use use.
     *
     * @param factory A factory
     */
    public void setFactory(final ResourceLoaderFactory factory) {
        this.factory = factory;
    }
}
