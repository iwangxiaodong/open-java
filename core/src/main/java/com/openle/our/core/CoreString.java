package com.openle.our.core;

public class CoreString {

//    System.out.println("*" + padWhitespaceLeft("moon", 12) + "*");
//    System.out.println("*" + padWhitespaceRight("moon", 12) + "*");
    public static String padWhitespaceLeft(String s, int len) {
        return String.format("%1$" + len + "s", s);
    }

    public static String padWhitespaceRight(String s, int len) {
        return String.format("%1$-" + len + "s", s);
    }

//  String左对齐
    public static String padLeft(String src, int len, char ch) {
        int diff = len - src.length();
        if (diff <= 0) {
            return src;
        }

        char[] charr = new char[len];
        System.arraycopy(src.toCharArray(), 0, charr, 0, src.length());
        for (int i = src.length(); i < len; i++) {
            charr[i] = ch;
        }
        return new String(charr);
    }

//  String右对齐
    public static String padRight(String src, int len, char ch) {
        int diff = len - src.length();
        if (diff <= 0) {
            return src;
        }

        char[] charr = new char[len];
        System.arraycopy(src.toCharArray(), 0, charr, diff, src.length());
        for (int i = 0; i < diff; i++) {
            charr[i] = ch;
        }
        return new String(charr);
    }

    public static String firstCharToLowerCase(String s) {
        if (s != null && !s.isEmpty()) {
            String c = s.substring(0, 1);
            s = s.replaceFirst(c, c.toLowerCase());
        }
        return s;
    }
}
