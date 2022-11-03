package com.openle.our.core;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

//  StackWalker.getInstance().forEach(System.out::println);
public class SourceCode {

    public static List<StackTraceElement> list() {
        return Arrays.asList(new Throwable().getStackTrace());
    }

    public String near() {
        return near(3);
    }

    public String near(int number) {
        String s = "";
        for (StackTraceElement ste : list().subList(0, number)) {
            s += " [" + ste.getClassName() + "." + ste.getMethodName() + ":" + ste.getLineNumber() + "]";
        }
        return s;
    }

    public static StackTraceElement current() {
        //list().subList(0, 3).forEach(System.out::println);
        return new LinkedList<>(list().subList(0, 3)).getLast();
    }

    private static Boolean isTest = null;

//  检测是否在运行单元测试
    public static Boolean isTest() {
        if (null == isTest) {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            List statckList = Arrays.asList(stackTrace);
            for (Iterator i = statckList.iterator(); i.hasNext();) {
                String stackString = i.next().toString();
                if (stackString.lastIndexOf("org.junit.") > -1) {
                    isTest = true;
                    System.out.println(stackString);
                    return isTest;
                }
            }
            isTest = false;
            return isTest;
        } else {
            return isTest;
        }
    }
}
