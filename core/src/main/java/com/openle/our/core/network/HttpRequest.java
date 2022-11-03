package com.openle.our.core.network;

import com.openle.our.core.io.IO;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.UUID;

//   todo - post + form-data 暂只有execute(...)才能使用。
/*
    HttpRequest.execute("https://example.com", "", "POST",
        "multipart/form-data; boundary=any-string", Map.of("k", "v"));
 */
public class HttpRequest {

    public static String get(String url) {
        return get(url, null);
    }

    public static String get(String url, String params) {
        return get(url, params, null);
    }

    public static String get(String url, String params, String contentType) {
        return execute(url, params, "GET", contentType, null);  //  todo - formDataMap暂设null
    }

    public static String post(String url) {
        return post(url, null);
    }

    public static String post(String url, String params) {
        return post(url, params, null);
    }

    public static String post(String url, String params, String contentType) {
        return execute(url, params, "POST", contentType, null);  //  todo - formDataMap暂设null
    }

    //  post + formDataMap暂不支持File文件入参
    public static String execute(String url, String params, String method,
            String contentType, Map<String, String> formDataMap) {
        String content = null;
        HttpURLConnection conn = null;
        try {
            URL urlObj = new URL(url);
            conn = (HttpURLConnection) urlObj.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);

            conn.setRequestMethod(method);
            conn.setUseCaches(false);

            var isFormData = false;
            String boundary = null;    //  --uuid
            if (contentType != null && !contentType.isBlank()) {
                var ct = contentType.trim();
                //  "application/x-www-form-urlencoded"
                //  multipart/form-data; boundary=--uuid
                conn.setRequestProperty("Content-Type", ct);
                if (ct.startsWith("multipart/form-data")) {
                    var key = "boundary=";
                    boundary = ct.substring(ct.indexOf(key) + key.length());
                    if (boundary == null || boundary.isBlank()) {
                        boundary = UUID.randomUUID().toString();
                    }
                    isFormData = true;
                }
            }
            System.out.println("boundary: " + boundary);
            if (params != null) {
                if (isFormData) {
                    StringBuilder sb = new StringBuilder();
                    if (formDataMap != null && !formDataMap.isEmpty()) {
                        for (var entry : formDataMap.entrySet()) {
                            System.out.println("key = " + entry.getKey() + ", value = " + entry.getValue());
                            //  必须比boundary定义时前部多出两个横线，否则报：Missing initial multi part boundary
                            sb.append("--").append(boundary).append("\r\n");
                            sb.append("Content-Disposition: form-data; name=\"")
                                    .append(entry.getKey()).append("\"\r\n\r\n");
                            sb.append(entry.getValue()).append("\r\n");
                        }
                    }

                    //  结束标记
                    sb.append("--").append(boundary).append("--\r\n");
                    try ( DataOutputStream dos = new DataOutputStream(conn.getOutputStream())) {
                        dos.write(sb.toString().getBytes("UTF-8")); // Java18+内置UTF-8后就不用设置编码了
                        dos.flush();
                    }
                    System.out.println(sb.toString());
                } else {
                    try ( DataOutputStream dos = new DataOutputStream(conn.getOutputStream())) {
                        dos.writeBytes(params);
                        dos.flush();
                    }
                }
            }

            content = IO.inputStreamToString(conn.getInputStream());

        } catch (IOException e) {
            System.err.println(e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return content;
    }

    public static Map.Entry<String, String> requestHttps(String urlString) {
        return requestHttps(urlString, true);
    }

    //  API 30+ use Map.Entry
    public static Map.Entry<String, String> requestHttps(String urlString, boolean useCaches) {
        String content = null, error = null;
        HttpURLConnection conn = null;
        try {
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setUseCaches(useCaches);
            var out = new ByteArrayOutputStream();
            IO.transferTo(conn.getInputStream(), out);
            content = out.toString();
            System.out.println("Contents of get request ends");

        } catch (MalformedURLException e) {
            error = e.getMessage();
        } catch (IOException e) {
            error = e.getMessage();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return Map.entry(content, error);
    }

    public static String requestGetHttpsCustomCA(String urlString) {
        //DeprecatedTLS.trustAll();
        try {
            var url = new URL(urlString);
            if ("https".equals(url.getProtocol())) {
                DeprecatedTLS.trustHostname(url.getHost());
            }
        } catch (MalformedURLException ex) {
            System.err.println(ex);
        }

        return get(urlString);
    }

//    public static ReturnStringAndError requestHttpsCustomCA(String urlString) {
//
//        ReturnStringAndError r = new ReturnStringAndError();
//        HttpURLConnection conn = null;
//        try {
//            URL url = new URL(urlString);
//            if ("https".equals(url.getProtocol())) {
//                DeprecatedTLS.trustHostname(url.getHost());
//            }
//
//            conn = (HttpURLConnection) url
//                    .openConnection();
//            String content = IO.inputStreamToString(conn.getInputStream());
//
//            r.setString(content);
//            System.out.println("Contents of get request ends");
//
//        } catch (MalformedURLException e) {
//            r.setError(e.getMessage());
//        } catch (IOException e) {
//            r.setError(e.getMessage());
//        } finally {
//            if (conn != null) {
//                conn.disconnect();
//            }
//        }
//
//        return r;
//    }
//
//    public static class ReturnStringAndError {
//
//        private String string;
//        private String error;
//
//        public String getError() {
//            return error;
//        }
//
//        public void setError(String error) {
//            this.error = error;
//        }
//
//        public String getString() {
//            return string;
//        }
//
//        public void setString(String string) {
//            this.string = string;
//        }
//
//    }
    // String cd = request.getPart("x").getHeader("Content-Disposition");
    public static String getFileName(String cd) {
        String fileName = null;
        if (cd != null) {
            fileName = cd.replaceFirst("(?i)^.*filename=\"?([^\"]+)\"?.*$", "$1");
            //fileName = URLDecoder.decode(fileName, StandardCharsets.ISO_8859_1);
        }
        return fileName;
    }

}
