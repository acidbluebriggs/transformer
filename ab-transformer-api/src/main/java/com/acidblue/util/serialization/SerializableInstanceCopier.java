package com.acidblue.util.serialization;

import java.io.Serializable;

/**
 * Copys an instance of a serializable object. 
 *
 *
 * @author <a href="mailto:acidblue@acidblue.com">Marc Miller (a.k.a. Briggs)</a>
 * @since 1.0
 */
public interface SerializableInstanceCopier {

    /**
     * Returns a full copy of the given instance.  Implementors of this method are required to return a full, deep
     * copy of the instance.  All references shall be copies.
     *  
     * @param instance An serializable instance
     * @return A full copy of instance
     */
    <T extends Serializable> T copy(T instance);
}
