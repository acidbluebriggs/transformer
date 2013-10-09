package com.acidblue.util;

/**
 * This class contains very simple validation routines for given string values.  Each method validates a given
 * string and completes cleanly if the validation passes. If the validation routine fails, the appropriate
 * exception will be thrown, such as an {@link IllegalArgumentException}.
 * <p/>
 * <p/>
 * <p/>
 * $HeadURL$
 * $Id$
 *
 * @author <a href="mailto:acidblue@acidblue.com">Marc Miller (a.k.a. Briggs)</a>
 * @since $LastChangedDate$
 *        <p/>
 *        $LastChangedBy$
 */
public final class StringValidator {

    /**
     * Utility Class
     */
    private StringValidator() {

    }

    /**
     * Validates that a String is not &quot;blank&quot;. &quot;blank&quot; is defined as being null, or containing
     * nothing but white space.
     *
     * @param name   The name of the parameter, so that it shows up in the exception
     * @param target The target string to test
     * @throws IllegalArgumentException if the given string was empty
     */
    public static void isNotBlank(final String name, final String target) {
        if (target == null || target.trim().isEmpty()) {
            throw new IllegalArgumentException(name == null ? "parameter" : name + " must be provided");
        }
    }

    public static boolean hasUpper(String s)
       {
        return s.matches(".*[A-Z].*");
       }

    public static boolean hasLower(String s)
       {
        return s.matches(".*[a-z].*");
       }

     public static boolean hasDigit(String s)
       {
        return s.matches(".*[0-9].*");
       }

      public static boolean hasSpecialChar(String s){
         return s.matches(".*[^0-9a-zA-Z].*");
      }

    public static boolean hasLetters(String s){
           return s.matches(".*[a-zA-Z].*");
    }


}
