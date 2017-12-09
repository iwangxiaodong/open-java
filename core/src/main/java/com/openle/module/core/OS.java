package com.openle.module.core;

public class OS {

    public static boolean isWindows() {
        String os = System.getProperty("os.name");
        System.out.println("os.name:" + os);
        if (os != null) {
            return os.toLowerCase().startsWith("windows");
        }
        return false;
    }
}
