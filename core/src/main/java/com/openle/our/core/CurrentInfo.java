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
        System.out.println("Temporary directory - " + System.getProperty("java.io.tmpdir"));

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
        System.out.println("JAVA_USER_OPTS - " + System.getenv("JAVA_USER_OPTS"));// 只存在于GAE?

        //  cc.isAssignableFrom(BasicComboBoxRenderer.class)
        var cc = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).getCallerClass();
        System.out.println("getCallerClass - " + cc);

        // 获取JVM命令行参数: [-Dfile.encoding=UTF-8, -Duser.language=zh]
        //System.out.println(java.lang.management.ManagementFactory.getRuntimeMXBean().getInputArguments());
        //
        // 获取操作系统已用内存量
        //var mxb = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        //System.out.println(mxb.getTotalMemorySize() - mxb.getFreeMemorySize());
    }
}
