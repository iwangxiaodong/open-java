package com.openle.our.core.converter;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Formatter;

public class HexConverter {

    public static void main(String[] s) {
        byte[] bytes = "中国".getBytes();
        //byte[] bytes = new byte[]{(byte) 0xAC, (byte) 0xED, (byte) 0x00, (byte) 0x05};
        System.out.println(byteArrayToHex(bytes));
        byte[] b2 = hexToByteArray(byteArrayToHex(bytes));
        System.out.println(new String(b2));
        System.out.println(Arrays.toString(b2));
    }

//    import javax.xml.bind.DatatypeConverter ;
//    public static String toHexString(byte[] array) {
//        return DatatypeConverter.printHexBinary(array);
//    }
//    public static byte[] toByteArray(String s) {
//        return DatatypeConverter.parseHexBinary(s);
//    }
//    
//    import javax.xml.bind.annotation.adapters.HexBinaryAdapter ;
//    public byte[] hexToBytes(String hexString) {
//        HexBinaryAdapter adapter = new HexBinaryAdapter();
//        byte[] bytes = adapter.unmarshal(hexString);
//        return bytes;
//    }
//
    public static String byteArrayToHex(final byte[] bytes) {
        String result;
        try (Formatter formatter = new Formatter()) {
            for (byte b : bytes) {
                formatter.format("%02x", b);
            }
            result = formatter.toString();
        }
        return result;
    }

    public static byte[] hexToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    //  该方法似乎受制于BigInteger的长度？
    public static byte[] shortHexToBytes(String hexString) {
        // 等同 Integer.valueOf(hexString, 16);
        return new BigInteger(hexString, 16).toByteArray();
    }

    public static String singleHexToString(String hexString) {
        return new String(Character.toChars(Integer.valueOf(hexString, 16)));
    }

//    public static final String byteArrayToHex(byte[] bArray) {
//        StringBuilder sb = new StringBuilder(bArray.length);
//        String sTemp;
//        for (int i = 0; i < bArray.length; i++) {
//            sTemp = Integer.toHexString(0xFF & bArray[i]);
//            if (sTemp.length() < 2) {
//                sb.append(0);
//            }
//            sb.append(sTemp.toUpperCase());
//        }
//        return sb.toString();
//    }
}
