package com.openle.our.core.security;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

//  或用bcprov-jdk15on的PemReader类
//  研究下 https://github.com/google/tink 加解密
public class OurPEM {

    //    只支持 PKCS#1 和 PKCS#8 公钥格式；暂不处理带密码PEM
    public static String pemPublicKeyToBase64(String pemPublicKeyString) {
        if (pemPublicKeyString != null && pemPublicKeyString.trim().length() > 0
                && pemPublicKeyString.startsWith("-----BEGIN")) {

            if (pemPublicKeyString.contains("BEGIN PUBLIC KEY")
                    && pemPublicKeyString.contains("END PUBLIC KEY")) {
                pemPublicKeyString = pemPublicKeyString.trim().replaceAll("-----BEGIN PUBLIC KEY-----", "")
                        .replaceAll("-----END PUBLIC KEY-----", "").replaceAll("\n", "").trim();

            } else if (pemPublicKeyString.contains("BEGIN RSA PUBLIC KEY")
                    && pemPublicKeyString.contains("END RSA PUBLIC KEY")) {
                pemPublicKeyString = pemPublicKeyString.trim().replaceAll("-----BEGIN RSA PUBLIC KEY-----", "")
                        .replaceAll("-----END RSA PUBLIC KEY-----", "").replaceAll("\n", "").trim();
            }
            return pemPublicKeyString;
        }
        return null;
    }

    public static PublicKey getPublicKeyByBase64PublicKey(String base64PublicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyBytes;
        keyBytes = Base64.getDecoder().decode(base64PublicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

}
