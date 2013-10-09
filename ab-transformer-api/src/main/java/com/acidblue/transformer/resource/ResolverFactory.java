package com.acidblue.transformer.resource;

/**
 * A simple factory class that returns an instance of Resolver.  This class allows a client to inject a single instance
 * of Resolver via the {@link #initialize(Resolver)} method.  Calling {@link #get()} returns the instance provided. This
 * is mainly used for classes that need a feeling of a static reference to an instance of a resolver.  Usage of this
 * class is questionable in new classes as the instance of a resolver should be injected into the instance requiring
 * access to a resolver (if using an IoC or DI container, of course).
 */
public final class ResolverFactory extends Resolver {

    private static Resolver resolver;

    /**
     * Sets the instance of Resolver to be used by this factory.
     *
     * @param instance The instance of Resolver this factory will provide to clients
     * @return The Resolver passed in
     * @throws IllegalStateException If the factory already contains an instance of Resolver
     */
    public static Resolver initialize(final Resolver instance) {


        if (resolver == null) {
            resolver = instance;
        } else {
            throw new IllegalStateException("Resolver has already been set");
        }

        return resolver;
    }

    /**
     * Returns an instance of Resolver.
     *
     * @return A resolver
     */
    public static Resolver get() {
        return resolver;
    }

    /**
     * A convenience that delegates the call to the underlying factory instance, equivalent to calling {@code
     * ResolverFactory.get().proxy(Some.class)}.
     *
     * @param resourceInterface The interface that contains the needed resources
     * @param <T> The type of the interface class
     * @return An instance of the provided interface
     */
    public static <T> T get(Class<T> resourceInterface) {
        return resolver.proxy(resourceInterface);
    }


}
