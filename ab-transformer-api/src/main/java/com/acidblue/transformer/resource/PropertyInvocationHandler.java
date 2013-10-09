package com.acidblue.transformer.resource;

import com.acidblue.transformer.resource.transformer.Transformable;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * An invocation handler that inspects the given method's return value, consults a {@link
 * ResourceFactory} for the value's type mapping and calls appropriate
 * transformer to return the expected value and type.
 * <p/>
 * If no value is returned from the resource factory, this class will attempt to locate the default value.
 * If no default is specified, {@code null} is returned (ew, null).
 * <p/>
 * <b>Note:</b> Users of this API should be very careful about not setting defaults on primitive types. If no default
 * value is specified and the Resource Loader returned a null, a {@code NullPointerException} will thrown because there
 * is no type conversion from a null to a primitive. There is no attempt made to return a default value in this case
 * as this could be too dangerous to assume a value.
 */
public class PropertyInvocationHandler implements InvocationHandler {

    private final Logger LOG = LoggerFactory.getLogger(PropertyInvocationHandler.class);

    private ResourceFactory factory;

    private final Map<Class, String> typeMapping = new HashMap<>();
    private final Map<Class<?>, Transformable<Object, Object>> decoders = new HashMap<>();
    private final Lock lock = new ReentrantLock();

    private final String KEY_FORMAT = "%s.%s.%s";
    private final String METHOD_KEY_FORMAT = "%s.%s";
    private final String FOR_KEY_FORMAT = KEY_FORMAT + ".%s";
    private final String FOR_METHOD_KEY_FORMAT = METHOD_KEY_FORMAT + ".%s";

    private Method currentMethod;
    private String defaultType;
    private Object[] currentParameters;

    //this is here as a performance enhancement. This is where over 30% of time is involved when querying
    //the method for it's annotations Looking it up once helps tremendously.
    private Annotation[][] currentParameterAnnotations;

    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {

        this.lock.lock();

        this.currentMethod = method;
        this.currentParameters = args;
        this.currentParameterAnnotations = currentMethod.getParameterAnnotations();

        try {
            return invoke();
        } finally {
            this.lock.unlock();
        }
    }

    /**
     * Returns the property value for the invoked method.
     *
     * @return The expected return value/type for the method
     * @throws Throwable Since it is using reflection
     */
    private Object invoke() throws Throwable {

        return isResourceDefined() ? decodeValue() : decodeDefault();
    }


    /**
     * Returns true if the specified resource's key exists for the specific loader.  This does not mean that the
     * resource has been defined a value, only that it's key exists
     *
     * @return true if the resource is defined.
     */
    @SuppressWarnings("unchecked")
    private boolean isResourceDefined() {

        return factory.exists(bundleKey(), getLoaderForClass());
    }

    private Object decodeValue() {

        return getDecoder().transform(getResourceValue());
    }

    private Object decodeDefault() {

        return canResolveDefault() ? getDecoder().transform(defaultValue()) :
                hasParameterValue() ? decodeParameter() : null;
    }

    private Object decodeParameter() {

        return hasDefaultParameterAnnotation() ? getDecoder().transform(parseDefaultParameterArray()) : null;
    }


    private String parseDefaultParameterArray() {
        final String[] values = currentDefaultParameterAnnotation().defs();
        final String currentValue = currentParameterValue();

        for (int i = 0; i < values.length; i += 2) {
            if (currentValue.equals(values[i])) {
                return values[i + 1];
            }
        }

        //we didn't find it see if there is an unknown
        return parseDefaultUnknownParameter();
    }


    private String parseDefaultUnknownParameter() {
        final String unknown = currentDefaultParameterAnnotation().unknown();

        return unknown.isEmpty() ? null : unknown;
    }

    //todo replace with a chain
    private String currentParameterValue() {

        LOG.debug("Resolving current parameter value for parameter {}", currentParameter());

        final String value;

        final Object currentParameter = currentParameter();

        if (currentParameter == null) {
            value = null;
        } else if (currentParameter instanceof Enum) {
            LOG.debug("Current parameter is an enum, looking up enum name");
            value = currentEnumName();

        } else if (currentDefaultParameterAnnotation().lookup().length == 0) {
            //this condition could be attempted by using a 'toString()' as a last resort. Should we?
            LOG.error("Current parameter is a java bean, but unable to lookup value due to no lookup method" +
                    " configured, throwing exception.");
            throw new IllegalArgumentException(String.format("Unable to interrogate instance of parameter due to no" +
                    " lookup method has been specified in the annotationprocessor for parameter type of: '%s'",
                    currentParameterClass()));
        } else {

            final StringBuilder builder = new StringBuilder();

            try {

                final String[] params = currentDefaultParameterAnnotation().lookup();

                for (int i = 0; i < params.length; i++) {
                    builder.append((String) MethodUtils.invokeExactMethod(currentParameter(), params[i], null));
                    if (i < params.length - 1) {
                        builder.append(".");
                    }
                }

                value = builder.toString();

            } catch (NoSuchMethodException noSuchMethod) {
                LOG.warn(String.format("Lookup method '%s' does not exist on class '%s'",
                        currentDefaultParameterAnnotation().lookup(), currentParameterClass()), noSuchMethod);
                throw new RuntimeException(noSuchMethod);
            } catch (IllegalAccessException iae) {
                LOG.warn(String.format("Method lookup for method named '%s' cannot be accessed on instance of '%s', " +
                        "is it not public?", currentDefaultParameterAnnotation().lookup(),
                        currentParameterClass()), iae);

                throw new RuntimeException(iae);
            } catch (InvocationTargetException ite) {
                LOG.warn(String.format("Unable to invoke method '%s' on '%s?",
                        currentDefaultParameterAnnotation().lookup(), currentParameterClass()), ite);
                throw new RuntimeException(ite);
            }
        }

        LOG.debug("Returning value {}", value);
        return value;
    }

    private String currentEnumName() {

        return ((Enum) currentParameter()).name();
    }


    private DefaultParam currentDefaultParameterAnnotation() {

        return (DefaultParam) this.currentParameterAnnotations[0][0];
    }

    private boolean hasDefaultParameterAnnotation() {

        return this.currentParameterAnnotations.length == 1 &&
                this.currentParameterAnnotations[0].length == 1 &&
                this.currentParameterAnnotations[0][0] instanceof DefaultParam;
    }

    private Transformable<Object, Object> getDecoder() {

        return decoders.get(returnType());
    }

    /**
     * Returns the default value of a method's @Default annotationprocessor.
     *
     * @return The String value of the annotationprocessor
     */
    private String defaultValue() {

        return currentMethod.getAnnotation(Default.class).value();//train wreck?
    }

    private boolean canResolveDefault() {

        return isAnnotatedForDefaultValue() && canDecodeReturnValue();
    }


    private String bundleKey() {

        return hasParameterValue() ? parameterToBundleKey() : methodToBundleKey();
    }


    private Class<?> returnType() {

        final Class<?> decoder;
        final Class forType = currentMethod.getReturnType();

        //we could probably move these type to the transformer's themselves and have them
        //register themselves. But, since this is a small subset, we can fix it later.
        if (forType == boolean.class) {
            decoder = Boolean.class;
        } else if (forType == long.class) {
            decoder = Long.class;
        } else if (forType == int.class) {
            decoder = Integer.class;
        } else if (forType == double.class) {
            decoder = Double.class;
        } else if (forType == float.class) {
            decoder = Float.class;
        } else {
            decoder = forType;
        }

        return decoder;
    }


    private boolean canDecodeReturnValue() {

        return decoders.get(returnType()) != null;
    }

    private String getTypeMapping() {
        return typeMapping.get(getLoaderContext());
    }

    private Class<?> getLoaderContext() {

        return hasParameterValue() ? currentParameter().getClass() : returnType();
    }

    private boolean hasTypeMapping() {

        return typeMapping.containsKey(getLoaderContext());
    }

    private boolean hasParameterValue() {

        return currentParameters != null && currentParameters.length == 1 && currentParameters[0] != null;
    }

    private Object currentParameter() {

        return currentParameters[0];
    }

    //we can only hope we don't store something that doesn't belong
    @SuppressWarnings("unchecked")
    private Object getResourceValue() {

        return factory.value(bundleKey(), getLoaderForClass());
    }

    /**
     * Returns null if none found.
     *
     * @return The loader name, may return null
     */
    private String findLoaderName() {

        final Class<?> context = getLoaderContext();

        LOG.debug("Locating loader for class: {}", context);

        for (final Class<?> entryKey : typeMapping.keySet()) {
            if (entryKey.isAssignableFrom(context)) {
                return typeMapping.get(entryKey);
            }
        }

        //todo -- refactor this out, it's a quick and dirty hack
        //Couldn't resolve transformer
        final Class<?> returnType = returnType();

        for (final Class<?> entryKey : typeMapping.keySet()) {
            if (entryKey.isAssignableFrom(returnType)) {
                return typeMapping.get(entryKey);
            }
        }

        LOG.warn("No transformer registered for type {}", context);

        return defaultType != null ? defaultType : null;
    }

    private boolean isAnnotatedForDefaultValue() {

        return currentMethod.getAnnotation(Default.class) != null;
    }

    private String getLoaderForClass() {

        return hasTypeMapping() ? getTypeMapping() : findLoaderName();
    }


    private String parameterToBundleKey() {

        final String key = currentDefaultParameterAnnotation().key();
        final String bundleKey = key.isEmpty() ?
                String.format(KEY_FORMAT, currentMethod.getDeclaringClass().getName(),
                        currentMethod.getName(), currentParameterValue()) :

                String.format(FOR_KEY_FORMAT, currentMethod.getDeclaringClass().getName(),
                        currentParameterClass().getSimpleName(), currentParameterValue(), key);

        LOG.debug("Bundle key for parameter is {}", bundleKey);

        return bundleKey;
    }


    private String methodToBundleKey() {

        final String key = currentMethod.getAnnotation(Default.class) != null ?
                currentMethod.getAnnotation(Default.class).key() : "";

        return key.isEmpty() ?
                String.format(METHOD_KEY_FORMAT, currentMethod.getDeclaringClass().getName(), currentMethod.getName()) :
                String.format(FOR_METHOD_KEY_FORMAT, currentMethod.getDeclaringClass().getName(),
                        currentMethod.getName(), key);


    }

    private Class currentParameterClass() {

        return currentParameter() == null ? null : currentParameter().getClass();
    }

    public void setDefaultType(final String defaultType) {

        this.defaultType = defaultType;
    }

    public void setFactory(final ResourceFactory factory) {

        this.factory = factory;
    }

    public void setTypeMapping(final Map<Class, String> typeMapping) {

        this.typeMapping.putAll(typeMapping);
    }

    public void setDecoders(final Map<Class<?>, Transformable<Object, Object>> decoders) {

        this.decoders.putAll(decoders);
    }
}
