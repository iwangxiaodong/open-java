package com.openle.our.core.converter;

import java.math.BigInteger;

public class BigIntegerConvert {

    public static void main(String[] sss) {
        System.out.println("main");
        BigIntegerConvert bic = new BigIntegerConvert();
        bic.toAnyConversion(new BigInteger("AA", 16), BigInteger.valueOf(16));
        System.out.println(bic.getToAnyConversion());//Output: AA

        //System.out.println(module.base.UuidUtils.uuidToBigInteger());
    }

    private static BigInteger toDecimalResult = BigInteger.ZERO;
    private String toAnyConversion = "";

    public BigInteger getToDecimalResult() {
        return toDecimalResult;
    }

    public void setToDecimalResult(BigInteger toDecimalResult) {
        BigIntegerConvert.toDecimalResult = toDecimalResult;
    }

    public String getToAnyConversion() {
        return toAnyConversion;
    }

    public void setToAnyConversion(String toAnyConversion) {
        this.toAnyConversion = toAnyConversion;
    }

    //十进制转换中把字符转换为数字
    private int changeToDec(char ch) {
        int num = 0;
        if (ch >= 'A' && ch <= 'Z') {
            num = ch - 'A' + 10;
        } else if (ch >= 'a' && ch <= 'z') {
            num = ch - 'a' + 36;
        } else {
            num = ch - '0';
        }
        return num;
    }

    //任意进制转换为10进制
    public void toDecimal(String input, int base) {
        BigInteger Bigtemp = BigInteger.ZERO, temp = BigInteger.ONE;
        int len = input.length();
        for (int i = len - 1; i >= 0; i--) {
            if (i != len - 1) {
                temp = temp.multiply(BigInteger.valueOf(base));
            }
            int num = changeToDec(input.charAt(i));
            Bigtemp = Bigtemp.add(temp.multiply(BigInteger.valueOf(num)));
        }
        //System.out.println(Bigtemp);
        //return Bigtemp;
        this.setToDecimalResult(Bigtemp);
    }

    //数字转换为字符
    private char changeToNum(BigInteger temp) {
        int n = temp.intValue();

        if (n >= 10 && n <= 35) {
            return (char) (n - 10 + 'A');
        } else if (n >= 36 && n <= 61) {
            return (char) (n - 36 + 'a');
        } else {
            return (char) (n + '0');
        }
    }

    //十进制转换为任意进制
    public void toAnyConversion(BigInteger Bigtemp, BigInteger base) {
        String ans = "";
        while (Bigtemp.compareTo(BigInteger.ZERO) != 0) {
            BigInteger temp = Bigtemp.mod(base);
            Bigtemp = Bigtemp.divide(base);
            char ch = changeToNum(temp);
            ans = ch + ans;
        }
        //return ans;
        this.setToAnyConversion(ans);
    }

    public void anyToAny(String input, int scouceBase,
            BigInteger targetBase) {
        toDecimal(input, scouceBase);
        toAnyConversion(this.getToDecimalResult(), targetBase);
    }
}
