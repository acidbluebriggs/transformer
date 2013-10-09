package com.acidblue.util;

import java.util.Date;

/**
 * A simple utility class for the repetitive task of creating defensive copies of dates.
 * $HeadURL$
 * $Id$
 *
 * @author <a href="mailto:acidblue@acidblue.com">Marc Miller (a.k.a. Briggs)</a>
 * @since $LastChangedDate$
 *        <p/>
 *        $LastChangedBy$
 */
public final class DefensiveDateUtils {


    /**
     * Utility Class
     */
    private DefensiveDateUtils() {
    }

    /**
     * Creates a defensive copy of the date passed.
     *
     * @param date A date
     * @return A copy of the given date, or null if the data passed was null
     */
    public static Date defendDate(final Date date) {
        return date != null ? new Date(date.getTime()) : null;
    }

}
