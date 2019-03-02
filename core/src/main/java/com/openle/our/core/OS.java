package com.openle.our.core;

public class OS {

    public static String newLine = System.lineSeparator();//System.getProperty("line.separator");

    public static boolean isWindows() {
        String os = System.getProperty("os.name");
        System.out.println("os.name:" + os);
        if (os != null) {
            return os.toLowerCase().startsWith("windows");
        }
        return false;
    }
}
