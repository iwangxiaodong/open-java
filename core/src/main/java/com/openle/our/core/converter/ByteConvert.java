package com.openle.our.core.converter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.math.BigInteger;

/**
 *
 * @author xiaodong
 */
public class ByteConvert {

//    public static void main(String[] s) throws IOException, Exception {
//        System.out.println("xxx");
//        byte[] b = ByteConvert.intToBytes(40);
//        int bb = ByteConvert.bytesToInt(b);
//        System.out.println("bb - " + bb);
//
////        System.out.println(HexConverter.bytesToHexString(b));
//        String hexString = "ff0c";
//        //System.out.println(new String(Character.toChars(0xff0c)));
//        int aaa = Integer.valueOf(hexString, 16);
//        System.out.println("aaa - " + aaa);
//
//        System.out.println("bbb- " + new BigInteger(hexString, 16));
//    }
    public static int bytesToInt(byte[] intBytes) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(intBytes);
        int my_int;
        try (ObjectInputStream ois = new ObjectInputStream(bis)) {
            my_int = ois.readInt();
        }
        return my_int;
    }

    public static byte[] intToBytes(int myInt) throws IOException {
        byte[] int_bytes;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            try (ObjectOutput out = new ObjectOutputStream(bos)) {
                out.writeInt(myInt);
            }
            int_bytes = bos.toByteArray();
        }
        return int_bytes;
    }

    //java 合并两个byte数组
    public static byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
        byte[] byte_3 = new byte[byte_1.length + byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }

    public static byte[] bigIntegerToBytes(BigInteger bigInteger) throws IOException {
        byte[] array = bigInteger.toByteArray();
        if (array[0] == 0) {
            byte[] tmp = new byte[array.length - 1];
            System.arraycopy(array, 1, tmp, 0, tmp.length);
            array = tmp;
        }
        return array;
    }

    public static BigInteger bytesToBigInteger(byte[] bytes) {
        return new BigInteger(1, bytes);
    }

    public static byte[] copyOfRange(byte[] source, int from, int to) {
        byte[] range = new byte[to - from];
        System.arraycopy(source, from, range, 0, range.length);
        return range;
    }
}
