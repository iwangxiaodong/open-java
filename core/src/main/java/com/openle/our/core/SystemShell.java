package com.openle.our.core;

import com.openle.our.core.io.IO;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * @author xiaodong
 */
//  单行脚本可直接调用：Runtime.getRuntime().exec("mkdir /tmp/abc");
//  SystemShell.executeReturn("echo","123456");
//  SystemShell.executeReturn("cmd","/C","echo","123");
//    注意 - ProcessBuilder的Varargs命令首个值不能存在空格.
public class SystemShell {

    public final static String[] SHELL_BINARY
            = OS.isWindows() ? new String[]{"cmd", "/C"} : new String[]{"sh", "-c"};

    public static void execute(String command) {
        executeFull(false, SHELL_BINARY[0], SHELL_BINARY[1], command);
    }

    //  执行并返回标准输出+错误信息
    public static String executeReturn(String command) {
        return executeFull(true, SHELL_BINARY[0], SHELL_BINARY[1], command);
    }

    //  执行不返回
    public static void execute(String... command) {
        executeFull(false, command);
    }

    //  执行并返回标准输出+错误信息
    public static String executeReturn(String... command) {
        return executeFull(true, command);
    }

    //  新开线程读取IO流，可解决缓冲区写满而阻塞的问题(有文章称阻塞只发生在getErrorStream()?)
    private static String executeFull(boolean isReturn, String... command) {
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);   //  合并错误流至标准输出中
        //pb.directory(new File("/data/data/com.example.myapplication/cache"));

        Process p = null;
        try {
            p = pb.start();
        } catch (IOException e) {
            System.err.println(e);
        }

        String output;
        if (isReturn && p != null) {
            output = IO.inputStreamToString(p.getInputStream(), null);
            try {
                int exitValue = p.waitFor();
                System.out.println("exitValue - " + exitValue);
                if (0 != exitValue) {
                    System.err.println("error code is:" + exitValue);
                    return null;
                }
            } catch (InterruptedException e) {
                Logger.getGlobal().severe(e.toString());
            }

            return output;
        }

        return null;
    }

}
