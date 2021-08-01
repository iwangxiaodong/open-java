package com.openle.our.core;

import java.nio.charset.Charset;
import java.time.OffsetDateTime;

/**
 *
 * @author xiaodong
 */
public class StartupInfo {

    public static void info() {
        System.out.println("OS name - " + System.getProperty("os.name"));
        System.out.println("HOSTNAME - " + System.getenv("HOSTNAME"));
        System.out.println("TZ - " + System.getenv("TZ"));
        System.out.println("Time zone - " + System.getProperty("user.timezone"));
        System.out.println("Current time - " + OffsetDateTime.now());
        System.out.println("Default charset - " + Charset.defaultCharset());

        System.out.println("Java version - " + Runtime.version().toString());
        System.out.println("Java home - " + System.getProperty("java.home"));
    }
}
