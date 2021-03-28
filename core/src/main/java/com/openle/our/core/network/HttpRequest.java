package com.openle.our.core.network;

import com.openle.our.core.io.IO;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpRequest {

    public static String get(String url) {
        return get(url, null);
    }

    public static String get(String url, String params) {
        return get(url, params, null);
    }

    public static String get(String url, String params, String contentType) {
        return execute(url, params, "GET", contentType);
    }

    public static String post(String url) {
        return post(url, null);
    }

    public static String post(String url, String params) {
        return post(url, params, null);
    }

    public static String post(String url, String params, String contentType) {
        return execute(url, params, "POST", contentType);
    }

    public static String execute(String url, String params, String method, String contentType) {
        String content = null;
        HttpURLConnection conn = null;
        try {
            URL urlObj = new URL(url);
            conn = (HttpURLConnection) urlObj.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // 此方法在正式链接之前设置才有效。
            conn.setRequestMethod(method);
            conn.setUseCaches(false);

            if (contentType != null && !contentType.trim().isEmpty()) {
                //  "application/soap+xml; charset=utf-8"
                conn.setRequestProperty("Content-Type", contentType.trim());
            }

            // 正式创建链接
            conn.connect();

            if (params != null) {
                try (DataOutputStream dos = new DataOutputStream(conn.getOutputStream())) {
                    dos.write(params.getBytes());
                    dos.flush();
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

    public static String httpGetWithoutCA(String url) {
        DeprecatedTLS.trustAll();
        return get(url);
    }

    public static ReturnStringAndError requestWithoutCA(String urlString) {

        DeprecatedTLS.trustAll();

        ReturnStringAndError r = new ReturnStringAndError();

        HttpURLConnection conn = null;
        try {
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url
                    .openConnection();
            String content = IO.inputStreamToString(conn.getInputStream());

            r.setString(content);
            System.out.println("Contents of get request ends");

        } catch (MalformedURLException e) {
            r.setError(e.getMessage());
        } catch (IOException e) {
            r.setError(e.getMessage());
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return r;
    }

    public static class ReturnStringAndError {

        private String string;
        private String error;

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public String getString() {
            return string;
        }

        public void setString(String string) {
            this.string = string;
        }

    }

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
