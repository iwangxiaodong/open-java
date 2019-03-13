package com.openle.our.core;

import java.util.Arrays;

/**
 * 序列化实现有SnakeYAML、ObjectOutputStream/ObjectInputStream、Eclipse Yasson
 *
 * @author 168
 */
public interface ObjectSerialization {

    //  返回的是new String(bytes)，后续考虑用hex取代之
    default String dump(Object obj) {
        //  若指定new String(...)的编码则务必保持一致。
        return new String(dumpToByteArray(obj));
    }

    //  如果是ObjectOutputStream,则会调用writeSystemHeader()在bytes开头写入AC ED 00 05
    byte[] dumpToByteArray(Object obj);

    //  实现中需要判断isBinary(...)来决定需不需要new String(bytes)
    Object load(byte[] bytes);

    //  输入参数不支持ObjectOutputStream的Binary数据
    //  loadPlainText
    default Object load(String text) {
        return load(text.getBytes());
    }

    //  null=AUTO
    public enum SerializationMode {
        AUTO, BINARY, TEXT;
    }

    SerializationMode serializationMode();

    //  Java Built-in - 根据字节头判断
    //  目前只支持ObjectOutputStream为Binary数据
    default boolean isBinary(byte[] bytes) {
        if (serializationMode().name().equals(SerializationMode.BINARY.name())) {
            return true;
        } else if (serializationMode().name().equals(SerializationMode.TEXT.name())) {
            return false;
        }

        if (bytes.length < 2) {
            return false;
        }

        byte[] header = new byte[]{(byte) 0xAC, (byte) 0xED};
        byte[] newHeader = new byte[]{bytes[0], bytes[1]};
        return Arrays.equals(newHeader, header);
    }
}
