package com.openle.our.core.security;

//SHA:安全散列算法,又称信息摘要,不可逆
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA {

    // SHA.digest("a".getBytes("UTF-8"));
    // result: ca978112ca1bbdcafac231b39a23dc4da786eff8147c4e72b9807785afee48bb
    public static String digest(byte[] bytes) {
        String s = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(bytes);
            s = getDigestValue(md.digest());
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);

        }
        return s;
    }

    // for small file
    public static String digest(InputStream in) {
        return digest(in, 1024 * 4);// 默认缓冲区大小为4KB
    }

    public static String digest(InputStream in, int bufferLength) {
        String s = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] buffer = new byte[bufferLength];// new byte[1024 * 1024 *
            // 10];
            int len = 0;
            while ((len = in.read(buffer)) > 0) {
                md.update(buffer, 0, len);
            }

            s = getDigestValue(md.digest());
        } catch (IOException | NoSuchAlgorithmException e) {
            System.out.println(e);
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
        }
        return s;

    }

    public static String getDigestValue(byte[] digest) {
        // String s = android.util._.Base64.encodeToString(digest,
        // Base64.NO_WRAP);

        StringBuilder output = new StringBuilder(32);
        for (int i = 0; i < digest.length; i++) {
            String temp = Integer.toHexString(digest[i] & 0xff);
            if (temp.length() < 2) {
                output.append("0");
            }
            output.append(temp);
        }

        return output.toString();
    }

}
