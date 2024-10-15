package com.openle.our.core.network;

import java.io.IOException;
import java.net.URI;
import java.security.cert.X509Certificate;
import java.util.AbstractMap;
import java.util.Date;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;

/**
 *
 * @author person
 */
public class NetSecurity {

    public static Map.Entry<Boolean, Date> checkHttpsCertExpired(String urlString) {
        try {
            var url = URI.create(urlString).toURL();
            var conn = (HttpsURLConnection) url.openConnection();
            conn.connect(); // 不连接则 getServerCertificates() 报：connection not yet open
            var certs = conn.getServerCertificates();
            var cert = (X509Certificate) certs[0]; // 遍历证书链？
            var d = cert.getNotAfter().before(new Date());
            return new AbstractMap.SimpleEntry<>(d, cert.getNotAfter()); // true=过期
        } catch (IOException ex) {
            System.err.println(ex);
        }
        return null;
    }
}
