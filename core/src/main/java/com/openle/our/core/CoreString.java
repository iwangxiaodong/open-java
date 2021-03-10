package com.openle.our.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    //  按固定长度和分隔符进行分拆
    public static String splitter(String s, int fixedLength, CharSequence cs) {
        String[] tokens = s.split("(?<=\\G.{" + fixedLength + "})");
        return String.join(cs, tokens);
    }

    public static String firstCharToLowerCase(String s) {
        if (s != null && !s.isEmpty()) {
            String c = s.substring(0, 1);
            s = s.replaceFirst(c, c.toLowerCase());
        }
        return s;
    }

    //驼峰转下划线
    public static String camelToUnderline(String param) {
        Pattern p = Pattern.compile("[A-Z]");
        if (param == null || param.equals("")) {
            return "";
        }
        StringBuilder builder = new StringBuilder(param);
        Matcher mc = p.matcher(param);
        int i = 0;
        while (mc.find()) {
            builder.replace(mc.start() + i, mc.end() + i, "_" + mc.group().toLowerCase());
            i++;
        }

        if ('_' == builder.charAt(0)) {
            builder.deleteCharAt(0);
        }
        return builder.toString();
    }
}
