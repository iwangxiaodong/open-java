package com.openle.our.core;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.nio.charset.Charset;
import java.time.OffsetDateTime;
import java.util.Locale;

/**
 *
 * @author xiaodong
 */
public class CurrentInfo {

    public static void show() {
        System.out.println("OS name - " + System.getProperty("os.name"));
        System.out.println("Hostname - " + System.getenv("HOSTNAME"));
        System.out.println("Username - " + System.getProperty("user.name"));
        System.out.println("Time zone env(TZ) - " + System.getenv("TZ"));
        System.out.println("Time zone - " + System.getProperty("user.timezone"));
        System.out.println("Current time - " + OffsetDateTime.now());
        System.out.println("Default charset - " + Charset.defaultCharset());    //  JDK18+已写死为UTF-8
        // LC_ALL 和 LANG - en_US 、 -Duser.country=CH -Duser.language=de
        System.out.println("Default locale - " + Locale.getDefault());

        System.out.println("Current directory - " + System.getProperty("user.dir"));

        //  Android编译时报Missing class java.lang.Runtime$Version
        //  System.out.println("Java version - " + Runtime.version().toString());
        try {
            var v = Class.forName("java.lang.Runtime$Version");
            var rv = MethodHandles.lookup().findStatic(Runtime.class, "version", MethodType.methodType(v));
            System.out.println("Java version - " + rv.invoke().toString());
        } catch (ReflectiveOperationException e) {
            System.err.println(e);
        } catch (Throwable ex) {
            System.err.println(ex);
        }

        System.out.println("Java home - " + System.getProperty("java.home"));
        System.out.println("Java total memory - " + Runtime.getRuntime().totalMemory());
        System.out.println("Java heap memory - " + Runtime.getRuntime().maxMemory());
        System.out.println("Java free memory - " + Runtime.getRuntime().freeMemory());
        System.out.println("JAVA_TOOL_OPTIONS - " + System.getenv("JAVA_TOOL_OPTIONS"));
    }
}
