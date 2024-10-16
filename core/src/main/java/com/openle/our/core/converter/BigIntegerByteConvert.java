/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openle.our.core.converter;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 * https://www.cnblogs.com/victor2302/p/11018189.html
 *
 * @author xiaodong
 */
public class BigIntegerByteConvert {

    public static void main(String[] args) {

        var r = new BigInteger("package.name".getBytes());
        System.out.println(r);  //  34780056399595376503295143269

        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);

        BigInteger bigInteger = new BigInteger(key);
        System.out.println("old:" + Arrays.toString(key));
        System.out.println(bigInteger);
        System.out.println("new:" + Arrays.toString(bigInteger.toByteArray()));
    }

    //  只能与bytesToBigInteger配套使用,BigInteger值正数模式
    public static byte[] bigIntegerToBytes(BigInteger bigInteger) throws IOException {
        byte[] array = bigInteger.toByteArray();
        if (array[0] == 0) {
            byte[] tmp = new byte[array.length - 1];
            System.arraycopy(array, 1, tmp, 0, tmp.length);
            array = tmp;
        }
        return array;
    }

    //  只能与bigIntegerToBytes配套使用,BigInteger值正数模式
    public static BigInteger bytesToBigInteger(byte[] bytes) {
        return new BigInteger(1, bytes);
    }

    public static byte[] toUnsignedByteArray(BigInteger value) {
        byte[] signedValue = value.toByteArray();
        if (signedValue[0] != 0x00) {
            throw new IllegalArgumentException("value must be a psoitive BigInteger");
        }
        return Arrays.copyOfRange(signedValue, 1, signedValue.length);
    }

    public static BigInteger fromUnsignedByteArray(byte[] value) {
        byte[] signedValue = new byte[value.length + 1];
        System.arraycopy(value, 0, signedValue, 1, value.length);
        return new BigInteger(signedValue);
    }
}
