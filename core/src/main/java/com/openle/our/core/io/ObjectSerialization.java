package com.openle.our.core.io;

import java.io.ObjectStreamConstants;
import java.nio.ByteBuffer;

/**
 * 序列化实现有SnakeYAML、ObjectOutputStream/ObjectInputStream、Eclipse Yasson
 *
 * @author 168
 */
//  如果是ObjectOutputStream,则会dump时调用writeSystemHeader()在bytes开头写入AC ED 00 05
public interface ObjectSerialization {

    //  返回的是new String(bytes)，后续考虑用hex取代之
    default String dump(Object obj) {
        //  若指定new String(...)的编码则务必保持一致。
        return new String(dumpToByteArray(obj));
    }

    byte[] dumpToByteArray(Object obj);

    //  实现中需要判断isBinary(...)来决定需不需要new String(bytes)
    Object load(byte[] bytes);

    //  输入参数不支持ObjectOutputStream的Binary数据
    //  loadPlainText
    default Object load(String text) {
        return load(text.getBytes());
    }

    //  null=AUTO
    public enum StreamMode {
        AUTO, BINARY, TEXT;
    }

    StreamMode streamMode();

    //  Java Built-in - 根据字节头判断
    //  目前只支持ObjectOutputStream为Binary数据
    default boolean isBinaryStream(byte[] bytes) {
        if (streamMode().name().equals(StreamMode.BINARY.name())) {
            return true;
        } else if (streamMode().name().equals(StreamMode.TEXT.name())) {
            return false;
        }

        if (bytes.length < 2) {
            return false;
        }

        byte[] nowHeader = new byte[]{bytes[0], bytes[1]};
        return ByteBuffer.wrap(nowHeader).getShort() == ObjectStreamConstants.STREAM_MAGIC;

        //byte[] header = new byte[]{(byte) 0xAC, (byte) 0xED};
        //return Arrays.equals(nowHeader, header);
    }
}
