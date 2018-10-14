package com.openle.our.core.converter;

import java.math.BigInteger;

//        System.out.println(HexConverter.bytesToHexString(b));
public class HexConverter {

    public static String singleHexStringToCharacter(String hexString) {
        return new String(Character.toChars(Integer.valueOf(hexString, 16)));
    }

    public static byte[] hexStringToBytes(String hexString) {
        // 等同 Integer.valueOf(hexString, 16);
        return new BigInteger(hexString, 16).toByteArray();
    }

    public static final String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2) {
                sb.append(0);
            }
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }
}
