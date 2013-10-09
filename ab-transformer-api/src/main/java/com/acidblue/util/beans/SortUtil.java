package com.acidblue.util.beans;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A simple utility class for sorting collections based on its contained beans'
 * properties.
 * <p/>
 *
 * <pre>
 *      //example bean
 *      public class PersonBean {
 *          private String firstName;
 *          private String lastName;
 * <p/>
 *          public PersonBean(String firstName, String lastName) {
 *              this.firstName = firstName;
 *              this.lastName  = lastName;
 * <p/>
 *          }
 * <p/>
 *          public final String getFirstName() {
 *              return this.firstName;
 *          }
 * <p/>
 *          public final String getLastName() {
 *              return this.lastName;
 *          }
 * <p/>
 *      List people = new LinkedList();
 *      people.add(new PersonBean("Benjamin", "Franklin"));
 *      people.add(new PersonBean("Ronald", "Reagan"));
 *      people.add(new PersonBean("George", "Bush"));
 *      people.add(new PersonBean("George", "Washington"));
 * <p/>
 *      //sort by last name
 *      SortUtil.sort(people, "lastName");
 * <p/>
 *      //sort by first name
 *      SortUtil.sort(people, "firstName");
 * <p/>
 *      //sort by last name and first name
 *      SortUtil.sort(people, "lastname", "firstName");
 * </pre>
 * <p/>
 *
 * @author briggs <a href="mailto:acidbriggs@gmail.com">acidbriggs@gmail.com</a>
 * @version 1.0
 */
public final class SortUtil {

    /**
     * This is a utility class
     */
    private SortUtil() {

    }

    /**
     * Sorts a List based on a JavaBean property common to the elements within
     * the Collection.
     *
     * @param target       The list ot be sorted
     * @param propertyName the property to sort by
     */

    // public static <T extends Comparable<? super T>> void sort(List<T> list) {
    public static void sort(final List target, String propertyName) {
        final Comparator comparator =
                ComparatorFactory.getComparator(propertyName);
        Collections.sort(target, comparator);
    }


    /**
     * Sorts a List based on a JavaBean property common to the elements within
     * the Collection.
     *
     * @param target        The list ot be sorted
     * @param majorProperty the first property to sort by
     * @param minorProperty the second property to sort by
     */
    public static void sort(final List target, String majorProperty,
            String minorProperty) {
        final Comparator composite =
                ComparatorFactory.getComparator(majorProperty, minorProperty);
        Collections.sort(target, composite);
    }
}