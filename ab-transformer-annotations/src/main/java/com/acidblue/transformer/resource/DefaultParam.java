package com.acidblue.transformer.resource;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines the default resource value of an enum parameter.
 * <p/>
 * For each enum value, there are exactly 2 entries within the {@link #defs()} parameter. Starting from {@code def[i]},
 * where {@code i=0}, {@code i} will aways be a key which maps to the enum's constant name and {@code [i+1]} will always
 * be the value of {@code i} (a plist).
 */
@Documented
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface DefaultParam {

    /**
     * The key/value entries as defaults for annotated enum.
     *
     * @return Default values for an enum
     */
    String[] defs() default {};

    /**
     * If set, this value will be used as the 'fall through' value to return if there is no match within the annotations
     * defs. This can aid if future enum values were added to the application, but the annotation processor was not
     * updated to  accommodate the new value.  This will not return empty strings as values. You must assume that the
     * value null was returned.
     *
     * @return The default value to use, or an empty string signifying no default is set.
     */
    String unknown() default "";

    /**
     * An optional string describing the the annotated field.  This is primarily used for documentation documentation
     * builders.
     *
     * @return A string describing the annotated method
     */
    String desc() default "";

    /**
     * Allows for disambiguating keys for multiple uses.  In some cases, it may be preferred to share the base key name
     * for multiple properties, but contain sub elements which map to the different types.
     * <p/>
     * Such as: <br /> {@code com.acidblue.pfl.myKey.tt.text=A tooltip com.acidblue.pfl.myKey.alt.text=Alternate Text
     * com.acidblue.pfl.myKey.text=The main text }
     *
     * @return Either the subtype name or blank if none
     */
    String key() default "";


    /**
     * Defines a method, or methods, on the parameter to interrogate for it's value. If set, the resolver will attempt
     * to invoke the given method(s) name (which must not contain any parameters), retrieve it's value(s) and map this
     * value to a resource key.
     * <p/>
     * In the case of multiple methods, each methods value will be separated by a '.' character.
     *
     * @return A method name, or an empty string if no method set
     */
    String[] lookup() default {};
}
