package com.openle.our.core.network;

import com.openle.our.core.io.IO;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpRequest {

    public static String httpGet(String url) {

        String content = null;
        HttpURLConnection conn = null;
        try {
            URL urlObj = new URL(url);
            conn = (HttpURLConnection) urlObj.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // 此方法在正式链接之前设置才有效。
            conn.setRequestMethod("GET");
            conn.setUseCaches(false);
            // 正式创建链接
            conn.connect();

            content = IO.inputStreamToString(conn.getInputStream());

        } catch (IOException e) {
            System.out.println("发送GET请求出现异常！" + e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return content;
    }

    public static String httpGetWithoutCA(String url) {
        DeprecatedTLS.trustAll();
        return httpGet(url);
    }

    public static ReturnStringAndError requestWithoutCA(String urlString) {

        DeprecatedTLS.trustAll();

        ReturnStringAndError r = new ReturnStringAndError();
        String content = null;
        HttpURLConnection conn = null;
        try {
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url
                    .openConnection();
            content = IO.inputStreamToString(conn.getInputStream());

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

    public static String httpPost(String url, String params) {
        String content = null;
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        try {
            URL urlObj = new URL(url);
            conn = (HttpURLConnection) urlObj.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);

            // 此方法在正式链接之前设置才有效。
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);
            // 正式创建链接
            conn.connect();

            dos = new DataOutputStream(conn.getOutputStream());
            dos.write(params.getBytes());
            dos.flush();

            content = IO.inputStreamToString(conn.getInputStream());

        } catch (IOException e) {
            System.out.println("发送POST请求出现异常！" + e);
        } finally {
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException ex) {
                    Logger.getLogger(HttpRequest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (conn != null) {
                conn.disconnect();
            }
        }

        return content;
    }

    //
    //part.getHeader(contentDisposition)
    // Header information
    //public static String contentDisposition = "Content-Disposition";
    // from org.apache.catalina.core > ApplicationPart.java
    @SuppressWarnings("unchecked")
    public static String getFileName(String cd) {
        String fileName = null;
        // String cd = getHeader("Content-Disposition");
        if (cd != null) {
            String cdl = cd.toLowerCase(Locale.ENGLISH);
            if (cdl.startsWith("form-data") || cdl.startsWith("attachment")) {
                ParameterParser paramParser = new ParameterParser();
                paramParser.setLowerCaseNames(true);
                // Parameter parser can handle null input
                Map<String, String> params = paramParser.parse(cd, ';');
                if (params.containsKey("filename")) {
                    fileName = params.get("filename");
                    if (fileName != null) {
                        fileName = fileName.trim();
                    } else {
                        // Even if there is no value, the parameter is present,
                        // so we return an empty file name rather than no file
                        // name.
                        fileName = "";
                    }
                }
            }
        }
        return fileName;
    }

}
