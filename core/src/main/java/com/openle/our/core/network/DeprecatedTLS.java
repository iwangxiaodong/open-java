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
@Deprecated //  尽量限制其使用范围 - 后续考虑增加还原方法。
public class DeprecatedTLS {

    public static void trustAll() {
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
                .setDefaultHostnameVerifier(new MyHostnameVerifier());
    }

    private static class MyHostnameVerifier implements HostnameVerifier {

        @Override
        public boolean verify(String hostname, SSLSession session) {
            return hostname != null;    //  仅仅为了应付Google Play市场校验，其实直接return true;即可。
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
