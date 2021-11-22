package com.openle.our.core.os;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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

    //  setenv("OUR_ENVIRONMENT", "development-jta");
    //  android.system.Os.setenv(...)
    public static void setEnv(String key, String value) {
        if (key == null) {
            return;
        }

        //  jdk11 - key.isBlank()
        if (key.trim().isEmpty()) {
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put(key, value);
        //  jdk9 - Map.of(key, value)
        Map<String, String> newEnv = Collections.unmodifiableMap(map);

        Class<?> processEnvironmentClass = null;
        try {
            processEnvironmentClass = Class.forName("java.lang.ProcessEnvironment");
        } catch (ClassNotFoundException ex) {
            System.err.println(ex);
        }

        if (processEnvironmentClass != null) {
            Field theEnvironmentField = null;
            Field theCaseInsensitiveEnvironmentField = null;
            try {
                theEnvironmentField = processEnvironmentClass.getDeclaredField("theEnvironment");
                theCaseInsensitiveEnvironmentField = processEnvironmentClass.getDeclaredField("theCaseInsensitiveEnvironment");
            } catch (NoSuchFieldException | SecurityException ex) {
                System.err.println(ex);
            }
            if (theEnvironmentField != null) {
                theEnvironmentField.setAccessible(true);
            }
            if (theCaseInsensitiveEnvironmentField != null) {
                theCaseInsensitiveEnvironmentField.setAccessible(true);
            }

            Map<String, String> env = null;
            Map<String, String> ciEnv = null;
            try {
                if (theEnvironmentField != null) {
                    env = (Map<String, String>) theEnvironmentField.get(null);
                }
                if (theCaseInsensitiveEnvironmentField != null) {
                    ciEnv = (Map<String, String>) theCaseInsensitiveEnvironmentField.get(null);
                }
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                System.err.println(ex);
            }

            if (env != null) {
                env.putAll(newEnv);
            }
            if (ciEnv != null) {
                ciEnv.putAll(newEnv);
            }

        }
    }
}
