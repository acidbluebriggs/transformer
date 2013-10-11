package com.acidblue.transformer.resource;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines a default return value of a method.
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Default {

    /**
     * The default value for the property returned.
     *
     * @return A string value
     */
    String value();


    /**
     * An optional string describing the the annotated method.  This is primarily used for
     * documentation builders.
     *
     * @return A string describing the annotated field
     */
    String desc() default  "";

    /**
     * An optional string which will be appended the value to the resource bundle's generated key.
     * Not recommended as the key generation should be sufficient.
     *
     * @return A string which may be of zero length
     */
    String key() default "";
}
