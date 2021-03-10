package com.openle.our.core.io;

import com.openle.our.core.converter.HexConverter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

//  支持Android
public class ObjectIO {

    public static <T extends Serializable> byte[] objectToBytes(T obj) throws IOException {
        byte[] bytes;
        try ( ByteArrayOutputStream b = new ByteArrayOutputStream();  ObjectOutputStream o = new ObjectOutputStream(b)) {

            o.writeObject(obj);
            bytes = b.toByteArray();

        }

        return bytes;
    }

    public static <T extends Serializable> T bytesToObject(byte[] bytes) throws IOException, ClassNotFoundException {
        Object obj;
        try ( ByteArrayInputStream b = new ByteArrayInputStream(bytes);  ObjectInputStream o = new ObjectInputStream(b)) {

            obj = o.readObject();

        }
        return (T) obj;
    }

    //  Object 和 HexString 仅用于低版本 Android; 其他情况应首选 java.util.Base64
    //  commons-codec Base64似乎在Android报NoSuchMethodError
    public static <T extends Serializable> String objectToHexString(T obj) throws IOException {
        byte[] bytes = objectToBytes(obj);
        return HexConverter.bytesToHex(bytes);
    }

    public static <T extends Serializable> T hexStringToObject(String s) throws IOException, ClassNotFoundException {
        byte[] bytes = HexConverter.hexToBytes(s);
        Object obj = bytesToObject(bytes);
        return (T) obj;
    }

}
