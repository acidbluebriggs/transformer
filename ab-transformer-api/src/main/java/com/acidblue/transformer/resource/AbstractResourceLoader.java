package com.acidblue.transformer.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;


//load the resource by it's name
//get the resource by it's name (ignore the suffix). Again, these resources
//could be in separate files, and probably should be but we'll leave them in for now.
//resource are loaded by the following convention

//class name (either full or simple). If there is a conflict you are screwed. But, you
//can also throw in the full inside the resource loaded.  But, you will always pass the class
//so if you have a conflict, you will need to key the resource by it's full name in the resource
//bundle.

//TODO this javadoc sucks --briggs
//TODO explain the building of the resource key in the main javadoc.


/**
 * <b>This javadoc probably makes no sense, I am working on it -- briggs</b>
 * <p/>
 * Resource loaders provide the ability to load text and not textual values from resource bundles.
 * <p/>
 * <p/>
 * Each instance of a resource loader allows the ability to load a specific type (icon, text, video etc). Resource
 * loaders are registered by a key which represents a type of resource.
 * <p/>
 * <p/>
 * Keys are handled by the java convention that states that class names should begin with a Capital letter and package
 * names should start with lower case.  This makes it easier for us to locate fully qualified class names or class names
 * that are in their simple form. Straying from this convention will cause runtime errors, so don't do it.
 * <p/>
 * <ul> <li>MyClass.alert.icon = blank.jpg</li> <li>com.acidblue.example.MyClass.alert.icon = blank.jpg</li> </ul>
 *
 * @param <T> Defines the type the instance of the loader will return on it's call to {@link #get(String)}
 */
public abstract class AbstractResourceLoader<T> implements ResourceLoader<T> {

    private final Logger LOG = LoggerFactory.getLogger(AbstractResourceLoader.class);

    private final Map<String, T> resourceCache = new HashMap<String, T>();

    private String binding;
    private String resourceBundleBaseName;
    private ResourceBundle.Control control;

    private boolean cacheEnabled = true;

    public AbstractResourceLoader() {
    }

    /**
     * Creates an instance of the resource loader with a custom control.
     *
     * @param resourceBundleBaseName The base name of the resource bundle
     * @param binding                The suffix of the type that this loader represents
     * @param control                A custom control to load the resource bundle
     */
    public AbstractResourceLoader(final String resourceBundleBaseName,
                                  final String binding,
                                  final ResourceBundle.Control control) {

        if (binding == null) {
            throw new NullPointerException("resourceSuffix was null");
        }

        if (resourceBundleBaseName == null) {
            throw new NullPointerException("resourceBundleBaseName was null");
        }

        this.resourceBundleBaseName = resourceBundleBaseName;
        this.binding = binding;
        this.control = control;

        LOG.debug(String.format("Resource Bundle Name: %s, Binding: %s, Control: %s",
                resourceBundleBaseName, binding, control));
    }


    /**
     * Creates an instance of a resource loader.
     *
     * @param resourceBundleBaseName The base name of the resource bundle
     * @param binding                The suffix of the type that this loader represents
     */
    public AbstractResourceLoader(final String resourceBundleBaseName,
                                  final String binding) {

        this(resourceBundleBaseName, binding, null);
    }

    /**
     * Returns the suffix of the resource key.
     *
     * @return The suffix of the resource
     */
    public String getBinding() {
        return binding;
    }

    /**
     * The base name of the resource bundle used by the resource loader.
     *
     * @return A non-null value
     */
    public String getResourceBundleBaseName() {
        return resourceBundleBaseName;
    }

    /**
     * Returns the resource for the given key
     *
     * @param resourceName The key of the resource to look up
     * @return The resource
     * @throws NullPointerException if the resourceName was null
     * @throws java.util.MissingResourceException
     *                              if the given resource key does not exist within the resource bundle
     */

    public final T get(final String resourceName) {
        if (resourceName == null) {
            throw new NullPointerException("resourceName was null");
        }

        return cacheEnabled ? fromCache(resourceName) : loadResource(resourceName);
    }

    private T fromCache(final String resourceName) {
        if (!resourceCache.containsKey(resourceName)) {

            final T resourceValue = loadResource(resourceName);

            this.resourceCache.put(resourceName, resourceValue);
        }
        return resourceCache.get(resourceName);
    }

    public boolean hasResource(final String resourceKey) {

        final String key = getFullyQualifiedResourceKey(resourceKey);
        final boolean value = getBundle().containsKey(key);

        if (LOG.isWarnEnabled() && !value) {
            LOG.warn("Keyed resource {} is not defined", key);
        }

        return value;
    }

    /**
     * Will return the resource value for the given key. The key is the base pattern for the resource. The loader will
     * append it's specific suffix to actually locate the value.
     *
     * @param resourceKey The key the resource is entered under
     * @return The value of the resource
     */
    protected String getResourceValue(final String resourceKey) {

        return getBundle().getString(getFullyQualifiedResourceKey(resourceKey));
    }

    /**
     * Returns the raw resource bundle referenced by this loader.
     *
     * @return A resource bundle
     */
    protected ResourceBundle getBundle() {

        return control == null ?
                ResourceBundle.getBundle(this.resourceBundleBaseName, Locale.getDefault()) :
                ResourceBundle.getBundle(this.resourceBundleBaseName, Locale.getDefault(),
                        getClass().getClassLoader(), this.control);
    }

    /**
     * Returns the full key used for the resource bundle. The full key is the resource bundle name, with the resource
     * suffix appended  (using a dot separator).
     *
     * @param resourceName Then name of the resource
     * @return The full entry key used within the resource bundle(s)
     */
    protected String getFullyQualifiedResourceKey(final String resourceName) {
        return resourceName + "." + this.getBinding();
    }


    public void setBinding(final String binding) {
        this.binding = binding;
    }

    public void setResourceBundleBaseName(final String resourceBundleBaseName) {
        this.resourceBundleBaseName = resourceBundleBaseName;
    }

    public void setControl(final ResourceBundle.Control control) {
        this.control = control;
    }

    public void setCacheEnabled(final boolean cacheEnabled) {
        this.cacheEnabled = cacheEnabled;
    }

    /**
     * Return the resource for the given key.
     *
     * @param key A string used as a key to lookup the given value within a resource bundle
     * @return The value of the key
     */
    protected abstract T loadResource(final String key);
}
