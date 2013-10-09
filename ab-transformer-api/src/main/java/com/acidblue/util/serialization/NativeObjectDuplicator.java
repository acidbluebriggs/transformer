package com.acidblue.util.serialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * An implementation of the Serializable Instance Copier. This implementation uses Java's 'native' ObjectInputStream and
 * ObjectOutput stream to serialize instances of Serializable object.  This 'brute-force' serialization allows easy
 * cloning of instances and ensures that a copy does not maintain any state/references with the original.
 * <p/>
 * <p/>
 * <strong>This class is not thread safe!</strong>
 * <p/>
 * If you wish to share among threads you must manually manage a locking mechanism around {@link
 * #copy(java.io.Serializable)} method.
 *
 * @author <a href="mailto:acidblue@acidblue.com">Marc Miller (a.k.a. Briggs)</a>
 * @since 1.0
 */
public class NativeObjectDuplicator implements SerializableInstanceCopier {

    private ByteArrayOutputStream byteArrayOutputStream;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inStream;

    private Object result;

    public NativeObjectDuplicator() {
        byteArrayOutputStream = new ByteArrayOutputStream();
    }

    /**
     * Creates a deep copy of the given serializable instance. No state is shared among the copy and the original.
     *
     * @param instance A serializable instance
     * @return The copy of the instance
     * @throws RuntimeException if an issue occurs (most likely wraps an IOException).
     */
    @SuppressWarnings("unchecked")
    public <T extends Serializable> T copy(final T instance) {

        prepareOutputStream();
        writeObject(instance);
        prepareInputStream();
        readObject();

        return (T) result;
    }

    private void readObject() {
        try {
            result = inStream.readUnshared();
        } catch (Exception exception) {
            throw new RuntimeException("Unable to read object", exception);
        }
    }

    private <T> void writeObject(T model) {
        try {
            this.outputStream.writeUnshared(model);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void prepareInputStream() {

        try {
            inStream = new ObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void prepareOutputStream() {
        try {
            byteArrayOutputStream.reset();
            outputStream = new ObjectOutputStream(byteArrayOutputStream);
        } catch (IOException ioe) {
            throw new RuntimeException("Unable to create output stream", ioe);
        }
    }
}
