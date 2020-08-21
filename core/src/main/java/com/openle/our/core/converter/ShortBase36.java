package com.openle.our.core.converter;

import java.math.BigInteger;

/**
 *
 * @author xiaodong
 */
public class ShortBase36 {

    public static void main(String[] args) throws Exception {
        System.out.println(base10ToBase36(1234567890));
        System.out.println(base36ToBase10("kf12oi"));

        System.out.println(bigIntegerToBase36(new BigInteger("141107123559123")));
        System.out.println(base36ToBase10("1e0npu37ir"));
        //后续测试UUID通过byte[]编码
    }

    public static String base10ToBase36(long value) {
        return Long.toString(value, 36);
    }

    public static BigInteger base36ToBase10(String base36) {
        return new BigInteger(base36, 36);
    }

    public static String bigIntegerToBase36(BigInteger bigInteger) {
        return bigInteger.toString(36);
    }
}
