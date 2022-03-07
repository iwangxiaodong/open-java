package com.openle.our.core.os;

//package com.openle.our.core;
//
//import com.openle.our.core.io.IO;
//import com.openle.our.core.tuple.Tuple;
//import com.openle.our.core.tuple.Tuple3;
//import java.io.BufferedReader;
//import java.io.DataOutputStream;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.UnsupportedEncodingException;
//import java.util.function.Function;
//import java.util.logging.Logger;
//
////  Use SystemShell
//@java.lang.Deprecated
//public class Shell {
//
//    private static String commandsPath = "";
//
//    public static void init() {
//        String v2 = Shell.runCommandAndReturn("type busybox").v2;
//        System.out.println("Shell init：" + v2);
//        if (v2.contains("busybox is")) {
//            Shell.commandsPath = "busybox";
//        }
//    }
//
//    // 未测试
//    public static boolean isRootShell() {
//        String r = Shell.runCommandAndReturn("id").v2;
//        System.out.println("a:" + r);
//        if (r.contains("uid=0(root)")) {
//            System.out.println("you are root!");
//            return true;
//        }
//        return false;
//    }
//
//    public static void setCommandsPath(String commandsPath) {
//        Shell.commandsPath = commandsPath;
//    }
//
//    public static String getCommandsPath() {
//        return Shell.commandsPath;
//    }
//
//    public static String full(String command) {
//        return (Shell.commandsPath + " " + command).trim();
//    }
//
//    public static Integer runCommand(String command) {
//        return runCommand(command, null);
//    }
//
//    public static Tuple3<Integer, String, String> runCommandAndReturn(String command) {
//        return runCommandAndReturn(command, null);
//    }
//
//    public static Integer runRootCommand(String command) {
//        return runRootCommand(command, null);
//    }
//
//    public static Tuple3<Integer, String, String> runRootCommandAndReturn(String command) {
//        return runRootCommandAndReturn(command, null);
//    }
//
//    //added callback
//    public static Integer runCommand(String command, Function<String, String> cb) {
//        Tuple3<Integer, String, String> t3 = runCommand(command, false, false, "sh", cb);
//
//        if (t3 == null || t3.v1 == null) {
//            return -1;
//        }
//
//        return t3.v1;
//    }
//
//    public static Tuple3<Integer, String, String> runCommandAndReturn(String command, Function<String, String> cb) {
//        return runCommand(command, true, false, "sh", cb);
//    }
//
//    public static Integer runRootCommand(String command, Function<String, String> cb) {
//        return runCommand(command, false, false, "su", cb).v1;
//    }
//
//    public static Tuple3<Integer, String, String> runRootCommandAndReturn(String command, Function<String, String> cb) {
//        return runCommand(command, true, false, "su", cb);
//    }
//
//    public static Tuple3<Integer, String, String> runCommand(String command, boolean isNeedResult, boolean isPure, String shOrSuPath, Function<String, String> cb) {
//        System.out.println("runCommand:command=" + command + "|isNeedResult=" + isNeedResult + "|isPure=" + isPure + "|suPath=" + shOrSuPath);
//
//        Process process = null;
//        BufferedReader stdOut = null;
//        //BufferedReader stdErr = null;
//        //Tuple3<Integer, String, String> t = Tuple.tuple(Integer.valueOf(-1), "", "Shell Default Info!");
//        Tuple3<Integer, String, String> t = Tuple.tuple(-1, "", "Shell Default Info!");
//
//        StringBuilder output = new StringBuilder();
//        StringBuilder error = new StringBuilder();
//        try {
//            if (OS.isWindows()) {
//                if (isPure) {
//                    process = Runtime.getRuntime().exec(command);
//                } else {
//                    String[] commands = new String[]{"sh", "-c", command};
//                    if (OS.isWindows()) {
//                        commands = new String[]{"cmd", "/C", "chcp 65001 & " + command};
//                    }
//                    process = Runtime.getRuntime().exec(commands);
//                }
//            } else {
//                process = Runtime.getRuntime().exec(shOrSuPath);
//                DataOutputStream os = new DataOutputStream(process.getOutputStream());
//                os.writeBytes(command + "\n");
//                os.writeBytes("exit\n");
//                os.flush();
//            }
//        } catch (IOException e) {
//            System.err.println("Shell IOException:");
//            System.err.println(e);
//        }
//
//        if (process == null) {
//            System.err.println("Shell Process is null!");
//            return t;
//        }
//
//        boolean isWindowsAndPure = OS.isWindows() && !isPure;
//        if (isNeedResult) {
//            try {
//                stdOut = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
//            } catch (UnsupportedEncodingException e) {
//                Logger.getGlobal().severe(e.toString());
//            }
//
//            String line;
//            if (stdOut != null) {
//                try {
//                    while ((line = stdOut.readLine()) != null) {
//                        if (isWindowsAndPure) {
//                            isWindowsAndPure = false;
//                            continue;
//                        }
//
//                        if (cb != null) {
//                            cb.apply(line); //cb.onExecute(line);
//                        } else {
//                            output.append(line).append('\n');
//                        }
//                    }
//                } catch (IOException e) {
//                    Logger.getGlobal().severe(e.toString());
//                }
//            }
//
//            error.append(IO.inputStreamToString(process.getErrorStream()));
//
//        }
//
//        try {
//            int exitValue = process.waitFor();
//            if (0 != exitValue) {
//                System.err.println("call shell failed. error code is :" + exitValue);
//            }
//        } catch (InterruptedException e) {
//            Logger.getGlobal().severe(e.toString());
//        }
//
//        t = Tuple.tuple(process.exitValue(), output.toString().trim(), error.toString().trim());
//
//        if (t.v3.length() > 0) {
//            System.out.println(t);
//        }
//
//        System.out.println("shell over.");
//        return t;
//    }
//}
