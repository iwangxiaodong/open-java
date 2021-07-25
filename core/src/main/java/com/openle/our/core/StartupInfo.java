/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openle.our.core;

import java.nio.charset.Charset;
import java.time.OffsetDateTime;

/**
 *
 * @author xiaodong
 */
public class StartupInfo {

    public static void info() {
        String tz = System.getenv("TZ");
        System.out.println("TZ - " + tz);
        System.out.println("user.timezone - " + System.getProperty("user.timezone"));
        System.out.println("datetime - " + OffsetDateTime.now());
        System.out.println("默认字符编码 - " + Charset.defaultCharset());
        System.out.println("Java version - " + Runtime.version().toString());
    }
}
