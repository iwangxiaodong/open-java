package com.openle.our.core.converter;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.UUID;

public abstract class UuidUtils {

//    public static void main(String[] args) {
//        String uuid = "386228d9-4088-44cb-a26e-b8c615016014";
//
//        String r = uuidToBase58(uuid);
//        System.out.println(r);
//        System.out.println(base58ToUuid(r));
//
//        r = uuidToBase32(uuid);
//        System.out.println(r);
//        System.out.println(base32ToUuid(r));
//    }
    public static BigInteger uuidToBigInteger() {
        UUID uuid = UUID.randomUUID();
        long hi = uuid.getMostSignificantBits();
        long lo = uuid.getLeastSignificantBits();
        byte[] bytes = ByteBuffer.allocate(16).putLong(hi).putLong(lo).array();
        BigInteger big = new BigInteger(1, bytes);
        return big;
    }

    public static String uuid() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public static String base58Uuid() {
        UUID uuid = UUID.randomUUID();
        return base58Uuid(uuid);
    }

    protected static String base58Uuid(UUID uuid) {

        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());

        return Base58.encode(bb.array());
    }

    public static String uuidToBase58(String uuidString) {
        UUID uuid = UUID.fromString(uuidString);
        return base58Uuid(uuid);
    }

    public static String base58ToUuid(String base58uuid) {
        byte[] byUuid = Base58.decode(base58uuid);
        ByteBuffer bb = ByteBuffer.wrap(byUuid);
        UUID uuid = new UUID(bb.getLong(), bb.getLong());
        return uuid.toString();
    }

    public static byte[] uuidTrim(String uuidString) {
        String r = uuidString.replaceAll("-", "");
        return new BigInteger(r, 16).toByteArray();
    }

    public static String uuidToBase32(String uuidString) {
        return Base32.encode(uuidTrim(uuidString));
    }

    public static String base32ToUuid(String base32uuid) {
        byte[] byUuid = Base32.decode(base32uuid);
        ByteBuffer bb = ByteBuffer.wrap(byUuid);
        UUID uuid = new UUID(bb.getLong(), bb.getLong());
        return uuid.toString();
    }
}
