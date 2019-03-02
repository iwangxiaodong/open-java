package com.openle.our.core;

import java.util.Arrays;

/**
 * 序列化实现有SnakeYAML、ObjectOutputStream/ObjectInputStream、Eclipse Yasson
 * @author 168
 */
public interface ObjectSerialization {

    //  如果是ObjectOutputStream,则会调用writeSystemHeader()在bytes开头写入AC ED 00 05
    byte[] dump(Object obj);

    //  如果是ObjectOutputStream则不需要"...".getBytes();
    Object load(byte[] bytes);

    //  不适用于ObjectOutputStream
    Object load(String text);

    //  Java Built-in - 根据字节头判断
    default boolean isJavaBuiltIn(byte[] bytes) {
        if (bytes.length < 2) {
            return false;
        }

        byte[] header = new byte[]{(byte) 0xAC, (byte) 0xED};
        byte[] newHeader = new byte[]{bytes[0], bytes[1]};
        return Arrays.equals(newHeader, header);
    }
}