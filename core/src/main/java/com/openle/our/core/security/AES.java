package com.openle.our.core.security;

//AES:对称加密算法，可逆，加解密用相同密钥，模式CBC，填充方式PKCS5Padding
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class AES {

    private IvParameterSpec ivSpec;
    private SecretKeySpec keySpec;

    public AES(String key) {
        try {
            byte[] keyBytes = key.getBytes();
            byte[] buf = new byte[16];

            for (int i = 0; i < keyBytes.length && i < buf.length; i++) {
                buf[i] = keyBytes[i];
            }

            this.keySpec = new SecretKeySpec(buf, "AES");
            this.ivSpec = new IvParameterSpec(keyBytes);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public byte[] encrypt(byte[] origData) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, this.keySpec, this.ivSpec);
            return cipher.doFinal(origData);
        } catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e) {
            System.out.println(e);
        }
        return null;
    }

    public byte[] decrypt(byte[] crypted) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, this.keySpec, this.ivSpec);
            return cipher.doFinal(crypted);
        } catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e) {
            System.out.println(e);
        }
        return null;
    }

//	public static void main(String[] args) throws Exception {
//        byte[] password = "wxd.1985".getBytes();
//        String key="                ";
//        //String key = SHA.digest("MybatisDataSourceFactory".getBytes())
//        //		.substring(0, 16);
//        //System.out.println(key);
//        AES aes = new AES(key);
//
//        byte[] crypted = aes.encrypt(password);
//        String s = Base64.encodeToString(crypted, Base64.NO_WRAP);
//        System.out.println(s);
//        System.out.println(new String(aes.decrypt(Base64.decode(s,
//                Base64.NO_WRAP))));
//	}
}
