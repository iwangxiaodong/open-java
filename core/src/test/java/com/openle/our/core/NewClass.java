/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openle.our.core;

import com.openle.our.core.network.HttpRequest;
import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 *
 * @author xiaodong
 */
public class NewClass {

    @Test
    public void temp() {
        var b = "test";// UUID.randomUUID().toString();
        var r = HttpRequest.execute("https://staticfiles.openle.com/.well-known/app/other/api/report-push-info", "", "POST",
                "multipart/form-data; boundary=WebKitFormBoundary7TMYhSONfkAM2z3a", Map.of(
                        "appId", "v",
                        "deviceId", "v2",
                        "addressingToken", "v3",
                        "userId", "中文测试123"));
        System.out.println(r);

        //System.out.println(HttpRequest.get("https://example.com"));
    }
}
