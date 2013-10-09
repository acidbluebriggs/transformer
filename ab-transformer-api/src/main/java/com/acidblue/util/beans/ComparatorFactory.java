package com.acidblue.util.beans;

import java.util.Comparator;

/**
 * A simple factory for creating Comparators for beans based on their
 * properties.
 *
 * @author briggs <a href="mailto:acidbriggs@gmail.com">acidbriggs@gmail.com</a>
 * @since Aug 30, 2005 - 12:10:28 PM
 */
public final class ComparatorFactory {

    /**
     * Private constructor due to this class being a singleton/utility
     */
    private ComparatorFactory() {

    }


    /**
     * Returns a comparator for a specified bean property.
     *
     * @param propertyName property name the comparator will compare to
     * @return A comparator instance
     */
    public static <T> Comparator<T> getComparator(final String propertyName) {
        if (propertyName == null) {
            throw new IllegalArgumentException("propertyName cannot be null");
        }

        return new BeanPropertyComparator<T>(propertyName);
    }


    /**
     * Returns a comparator that will compare properties based on two inputs.
     *
     * @param majorProperty the first property to for the sort
     * @param minorProperty the secondary sort property
     * @return a comparator instance
     */
    public static <T> Comparator<T> getComparator(String majorProperty,
            String minorProperty) {
        if (majorProperty == null) {
            throw new IllegalArgumentException("majorProperty cannot be null");
        }
        if (minorProperty == null) {
            throw new IllegalArgumentException("minor cannot be null");
        }

        final BeanPropertyComparator<T> bc1;
        final BeanPropertyComparator<T> bc2;
        
        bc1 = new BeanPropertyComparator<T>(majorProperty);
        bc2 = new BeanPropertyComparator<T>(minorProperty);

        return new CompositeComparator<T>(bc1, bc2);
    }
}