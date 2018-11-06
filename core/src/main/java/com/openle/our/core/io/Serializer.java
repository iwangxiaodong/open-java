package com.openle.our.core.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Serializer {

    public static <T extends Serializable> byte[] objectToBytes(T obj) throws IOException {
        byte[] bytes;
        try (ByteArrayOutputStream b = new ByteArrayOutputStream();
                ObjectOutputStream o = new ObjectOutputStream(b)) {

            o.writeObject(obj);
            bytes = b.toByteArray();

        }

        return bytes;
    }

    public static <T extends Serializable> T bytesToObject(byte[] bytes) throws IOException, ClassNotFoundException {
        Object obj;
        try (ByteArrayInputStream b = new ByteArrayInputStream(bytes);
                ObjectInputStream o = new ObjectInputStream(b)) {

            obj = o.readObject();

        }
        return (T) obj;
    }
}
