package com.acidblue.transformer.resource;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A factory for creating proxy implementations of an interface.
 */
public class Resolver {

    private final Logger LOG = LoggerFactory.getLogger(Resolver.class);

    private Map<Class<?>, Object> map = new ConcurrentHashMap<Class<?>, Object>();

    private Map<String, Map<ParameterKey, MethodInvocationHandler>> methodHandlers =
            new ConcurrentHashMap<String, Map<ParameterKey, MethodInvocationHandler>>();

    //TODO, we need a factory for the invocation handlers, inspect the params and decide
    private InvocationHandler invocationHandler;


    /**
     * Creates the default instance of resolver. The resolver has default method implementations of {@code toString},
     * {@code hashCode}, {@code equals} and {@code getClass}.
     */
    public Resolver() {

        LOG.debug("<clinit>");

        setMethodInvocationHandlers(
                Arrays.asList(
                        new ToStringInvoker(),
                        new EqualsInvocationHandler(),
                        new HashCodeInvocationHandler(),
                        new GetClassInvocationHandler()
                )
        );
    }

    /**
     * Factory that returns an instance of the given interface.
     *
     * @param resourceInterface The interface to proxy
     * @param <T>               The class type
     * @return A proxy instance of the given interface
     * @throws IllegalArgumentException - if any of the restrictions on the interface passed that may are violated
     */
    @SuppressWarnings("unchecked")
    //we put the type in the map, we know we are safe, right?
    public <T> T proxy(final Class<T> resourceInterface) {

        LOG.debug("Looking proxy instance for interface {}", resourceInterface);

        final T instance;

        if (!map.containsKey(resourceInterface)) {

            LOG.debug("Creating new instance of proxy interface {}", resourceInterface);
            map.put(resourceInterface,
                    Proxy.newProxyInstance(
                            this.getClass().getClassLoader(),
                            new Class[]{resourceInterface},
                            createProxy()
                    ));
        }

        instance = (T) map.get(resourceInterface);

        LOG.debug("Returning instance {}", resourceInterface);

        return instance;
    }


    /**
     * Returns true if the instance of Resolver has a configured invocation handler for the given method.
     *
     * @param method The method to inspect to see if it implemented.
     * @return true if a method handler exists for the given method's signature
     */
    private boolean isMethodCaptured(final Method method) {

        final String methodName = method.getName();

        return methodHandlers.containsKey(methodName) &&
                methodHandlers.get(methodName).containsKey(new ParameterKey(method.getParameterTypes()));
    }

    private InvocationHandler createProxy() {

        return new InvocationHandler() {
            public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {

                final InvocationHandler delegate = lookupHandler(method);

                return delegate.invoke(proxy, method, args);
            }
        };
    }

    private InvocationHandler lookupHandler(final Method method) {

        final InvocationHandler handler;

        if (isMethodCaptured(method)) {
            handler = this.methodHandlers.get(method.getName()).get(
                    new ParameterKey(method.getParameterTypes()));
        } else {
            handler = this.invocationHandler;
        }

        return handler;
    }


    /**
     * Sets client defined method invocation handlers.
     *
     * @param methodInvocationHandlers Any custom invocation handlers
     */
    public void setMethodInvocationHandlers(final List<MethodInvocationHandler> methodInvocationHandlers) {

        for (final MethodInvocationHandler handler : methodInvocationHandlers) {

            final String methodName = handler.getMethodName();

            if (!this.methodHandlers.containsKey(methodName)) {
                this.methodHandlers.put(methodName, new ConcurrentHashMap<ParameterKey, MethodInvocationHandler>());
            }

            final Map<ParameterKey, MethodInvocationHandler> methodHandlers = this.methodHandlers.get(methodName);

            methodHandlers.put(new ParameterKey(handler.getParameterTypes()), handler);
        }

    }

    /**
     * Need a hashtable key for storing class[]. Unfortunately,
     * <p/>
     * {@code new Class[0].equals(new Class[0])} resolves to {@code true} but {@code new Class[0].hashCode() == new
     * Class[0]} resolves to {@code false}. So, we cannot use the parameter list as a hashtable key.
     */
    private class ParameterKey {

        final Class[] parameters;

        final int hash;

        public ParameterKey(final Class[] parameters) {

            this.parameters = Arrays.copyOf(parameters, parameters.length);
            this.hash = calculateHashCode();
        }

        private int calculateHashCode() {

            int hashCode = 0;
            for (final Class parameter : parameters) {
                hashCode += parameter.hashCode();
            }

            return hashCode;
        }


        @Override
        public int hashCode() {

            return hash;
        }

        @Override
        public boolean equals(final Object key) {

            return key instanceof ParameterKey && Arrays.equals(parameters, ((ParameterKey) key).parameters);
        }
    }

    public void setInvoker(final InvocationHandler invocationHandler) {

        this.invocationHandler = invocationHandler;
    }

}

abstract class MethodInvocationHandler implements InvocationHandler {

    private final String methodName;
    private final Class[] parameterTypes;

    private static final Class[] noParameters = new Class[0];

    public MethodInvocationHandler(final String methodName, final Class[] parameterTypes) {

        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
    }

    public MethodInvocationHandler(final String methodName) {

        this.methodName = methodName;
        this.parameterTypes = noParameters;
    }

    public String getMethodName() {

        return methodName;
    }

    public Class[] getParameterTypes() {

        return Arrays.copyOf(parameterTypes, parameterTypes.length);
    }

    @Override
    public final Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {

        if (method.getName().equals(this.methodName) &&
                Arrays.equals(parameterTypes, method.getParameterTypes())) {

            return doInvoke(proxy, method, args);
        }

        throw new IllegalArgumentException();
    }

    public abstract Object doInvoke(Object proxy, Method method, Object[] args) throws Throwable;
}

class ToStringInvoker extends MethodInvocationHandler {

    public ToStringInvoker() {

        super("toString");
    }

    @Override
    public Object doInvoke(Object proxy, Method method, Object[] args) throws Throwable {

        return ReflectionToStringBuilder.reflectionToString(proxy);
    }
}

class GetClassInvocationHandler extends MethodInvocationHandler {

    public GetClassInvocationHandler() {

        super("getClass");
    }

    @Override
    public Object doInvoke(Object proxy, Method method, Object[] args) throws Throwable {

        return method.getDeclaringClass().getClass();
    }
}

class HashCodeInvocationHandler extends MethodInvocationHandler {

    public HashCodeInvocationHandler() {

        super("hashCode");
    }

    @Override
    public Object doInvoke(Object proxy, Method method, Object[] args) throws Throwable {

        return HashCodeInvocationHandler.class.toString().hashCode();
    }
}

class EqualsInvocationHandler extends MethodInvocationHandler {

    public EqualsInvocationHandler() {

        super("equals");
    }

    @Override
    public Object doInvoke(Object proxy, Method method, Object[] args) throws Throwable {

        return true;
    }


}

