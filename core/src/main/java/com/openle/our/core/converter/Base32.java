package com.openle.our.core.converter;

import java.math.BigInteger;


public class Base32 {

    public static void main(String[] args) throws Exception {

        System.out.println(UuidUtils.uuidToBase32("13d4b615-acbe-478e-9c14-6fd9c2cb9e01"));
        System.out.println(UuidUtils.base32ToUuid("cpklmfnmxzdy5haun7m4fs46ae"));

        System.out.println(Base32.encode("foo".getBytes("UTF-8")));
        System.out.println(new String(Base32.decode("mzxw6"), "UTF-8"));

//        System.out.println(Base32.encode(ByteConvert.bigIntegerToBytes(new BigInteger("141107123559123"))));
//        System.out.println(ByteConvert.bytesToBigInteger(Base32.decode("qbla76m22m")));

//        //for guava com/google/common/io/BaseEncoding.java
//        r = BaseEncoding.base32().omitPadding().lowerCase().encode("foo".getBytes("UTF-8"));
//        System.out.println(r);
//        System.out.println(new BigInteger(BaseEncoding.base32().lowerCase().decode(r)).toString(16));
    }

    private static final char[] ALPHABET = {
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
        'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
        'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
        'Y', 'Z', '2', '3', '4', '5', '6', '7'
    };

    private static final byte[] DECODE_TABLE;

    static {
        DECODE_TABLE = new byte[128];

        for (int i = 0; i < DECODE_TABLE.length; i++) {
            DECODE_TABLE[i] = (byte) 0xFF;
        }
        for (int i = 0; i < ALPHABET.length; i++) {
            DECODE_TABLE[(int) ALPHABET[i]] = (byte) i;
            if (i < 24) {
                DECODE_TABLE[(int) Character.toLowerCase(ALPHABET[i])] = (byte) i;
            }
        }
    }

    public static String encode(byte[] data) {
        //System.out.println("bytesToHexString:"+UuidUtils.bytesToHexString(data));
        char[] chars = new char[((data.length * 8) / 5) + ((data.length % 5) != 0 ? 1 : 0)];

        for (int i = 0, j = 0, index = 0; i < chars.length; i++) {
            if (index > 3) {
                int b = data[j] & (0xFF >> index);
                index = (index + 5) % 8;
                b <<= index;
                if (j < data.length - 1) {
                    b |= (data[j + 1] & 0xFF) >> (8 - index);
                }
                chars[i] = ALPHABET[b];
                j++;
            } else {
                chars[i] = ALPHABET[((data[j] >> (8 - (index + 5))) & 0x1F)];
                index = (index + 5) % 8;
                if (index == 0) {
                    j++;
                }
            }
        }

        return new String(chars).toLowerCase();
    }

    public static byte[] decode(String s) {
        if (s == null || s.isEmpty()) {
            return null;
        }

        s = s.toUpperCase();
        char[] stringData = s.toCharArray();
        byte[] data = new byte[(stringData.length * 5) / 8];

        for (int i = 0, j = 0, index = 0; i < stringData.length; i++) {
            int val;

            try {
                val = DECODE_TABLE[stringData[i]];
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new RuntimeException("Illegal character");
            }

            if (val == 0xFF) {
                throw new RuntimeException("Illegal character");
            }

            if (index <= 3) {
                index = (index + 5) % 8;
                if (index == 0) {
                    data[j++] |= val;
                } else {
                    data[j] |= val << (8 - index);
                }
            } else {
                index = (index + 5) % 8;
                data[j++] |= (val >> index);
                if (j < data.length) {
                    data[j] |= val << (8 - index);
                }
            }
        }

        return data;
    }
}
