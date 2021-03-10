package com.openle.our.core.converter;

import com.openle.our.core.CoreString;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Formatter;

public class HexConverter {

    public static void main(String[] s) {
        byte[] bytes = "中国".getBytes();

        System.out.println(bytesToHex(bytes));
        String upper = bytesToWellFormedHex(bytes);
        System.out.println(upper);
        byte[] b2 = hexToBytes(upper);
        System.out.println(new String(b2));
//
//        byte[] bytes3 = new byte[]{(byte) 0xAC, (byte) 0xED, (byte) 0x00, (byte) 0x05};
//        System.out.println(bytesToHex(bytes3));
//        byte[] b3 = shortHexToBytes(bytesToHex(bytes3));
//        System.out.println(new String(b3));

        System.out.println(Arrays.toString(b2));
    }

    //  全大写且空格分割 - E4 B8 AD E5 9B BD
    public static String bytesToWellFormedHex(final byte[] bytes) {
        return CoreString.splitter(bytesToHex(bytes), 2, " ").toUpperCase();
    }

    /*        
        javax.xml.bind.DatatypeConverter.printHexBinary(bytes); DatatypeConverter.parseHexBinary(hexString);
        new javax.xml.bind.annotation.adapters.HexBinaryAdapter().unmarshal(hexString);
        库 - implementation 'at.favre.lib:bytes:1.4.0'        
     */
    public static String bytesToHex(final byte[] bytes) {
        String result;
        try ( Formatter formatter = new Formatter()) {
            for (byte b : bytes) {
                // 格式化并追加,全小写且无空格, %02X则返回全大写
                formatter.format("%02x", b);
            }
            result = formatter.toString();
        }
        return result;
    }

    // 支持大小写及带空格入参
    public static byte[] hexToBytes(String hex) {
        if (hex.contains(" ")) {
            hex = hex.replace(" ", "");
        }

        // 等同 Integer.valueOf(hexString, 16);
        byte[] bytes = new BigInteger(hex, 16).toByteArray();
        int srcPos = (bytes[0] == 0) ? 1 : 0;   //  数组首个值表示正负标志，需要移除
        return Arrays.copyOfRange(bytes, srcPos, bytes.length);
    }

//        //  入参大小写均可，带不带空格均可
//    public static byte[] hexToBytes(String s) {
//        if (s.contains(" ")) {
//            s = s.replace(" ", "");
//        }
//
//        int len = s.length();
//        byte[] data = new byte[len / 2];
//        for (int i = 0; i < len; i += 2) {
//            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
//                    + Character.digit(s.charAt(i + 1), 16));
//        }
//        return data;
//    }
//
//    public static String singleHexToInteger(String hexString) {
//        return new String(Character.toChars(Integer.valueOf(hexString, 16)));
//    }
//    public static final String bytesToHex(byte[] bArray) {
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
