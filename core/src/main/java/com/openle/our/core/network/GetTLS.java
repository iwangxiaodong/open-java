package com.openle.our.core.network;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

public class GetTLS {

    public static SSLSocketFactory getSsf_(InputStream in, String passphrase) {
        try {
            return getSsf(in, passphrase);
        } catch (KeyManagementException | KeyStoreException
                | NoSuchAlgorithmException | NoSuchProviderException
                | CertificateException | IOException e) {
            System.out.println(e);

        }
        return null;
    }

    public static SSLSocketFactory getSsf(InputStream in, String passphrase)
            throws KeyStoreException, NoSuchAlgorithmException,
            NoSuchProviderException, KeyManagementException,
            CertificateException, IOException {

        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(in, passphrase.toCharArray());
        in.close();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        tmf.init(ks);
        TrustManager tms[] = tmf.getTrustManagers();
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tms, new java.security.SecureRandom());
        SSLSocketFactory ssf = sslContext.getSocketFactory();

        return ssf;
    }

//    public static void main(String[] args) throws IOException {
//        System.setProperty("javax.net.ssl.trustStore", "src/main/java/java.startcom.cacert");// ClassPath or AbsolutePath
//        System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
//        String url = "https://wangxiaodong.com/";
//        final String info = IOUtils.toString(
//                new URL(url), "UTF-8");
//        System.out.println(info);
//
//        //httpsConn.setSSLSocketFactory(getSsf_(getAssets().open("startcomca.store"),"changeit"));
//    }
}
