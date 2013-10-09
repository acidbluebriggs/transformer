package com.acidblue.transformer.resource;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotationprocessor to instruct the runtime that the interface represents a reference to external resources.  This
 * annotationprocessor can aid in external tools that need to inspect code, and perhaps, generate configuration files for the
 * Resource.
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({ElementType.TYPE})
public @interface Resource {

    /**
     * A description of the resource; used for generating documentation.
     *
     * @return A descriptive string (or empty if not provided)
     */
    String value() default "";

}
