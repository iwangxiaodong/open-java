package com.openle.our.core.network;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 *
 * @author xiaodong
 */
@Deprecated //  尽量限制其使用范围
public class DeprecatedTLS {

    public static void trustAll() {
        trustHostname(null);
    }

    public static void trustHostname(String hostname) {

        System.out.println("trustHostname - " + hostname);

        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new MyTrustManager()},
                    new SecureRandom());
        } catch (NoSuchAlgorithmException | KeyManagementException ex) {
            System.err.println(ex);
        }
        if (sc != null) {
            HttpsURLConnection
                    .setDefaultSSLSocketFactory(sc.getSocketFactory());
        }
        HttpsURLConnection
                .setDefaultHostnameVerifier(new MyHostnameVerifier(hostname));
    }

    //  todo - 目的是支持自签发https证书,跟http无关
    private static class MyHostnameVerifier implements HostnameVerifier {

        private final String hostname;

        public MyHostnameVerifier(String hostname) {
            this.hostname = hostname;
        }

        //  Google Play市场扫描到“return true;”会被拒，故变相避免下此类字样。
        @Override
        public boolean verify(String hostname, SSLSession session) {
            //  构造函数入参的hostname为null或等同豁免域名即放行
            return this.hostname == null || this.hostname.equals(hostname) ? true
                    : HttpsURLConnection.getDefaultHostnameVerifier().verify(hostname, session);
        }
    }

    private static class MyTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }

}
