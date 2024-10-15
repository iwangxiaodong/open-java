package com.openle.our.core.converter;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.HexFormat;
import java.util.UUID;

public abstract class UuidUtils {

    public static void main(String[] args) {
        String uuidString = "14694875-6bc8-11ef-b3b9-1d86da4ff488";

        BigInteger bi = uuidStringToBigInteger(uuidString);
        System.out.println("bi - " + bi);

        var uuid = bigIntegerToUUID(bi);
        System.out.println("uuid - " + uuid);

        System.out.println(uuidToHex(UUID.fromString(uuidString)));

        String r = uuidToBase58(uuidString);
        System.out.println(r);
        System.out.println(base58ToUuid(r));

        r = uuidToBase32(uuidString);
        System.out.println(r);
        System.out.println(base32ToUuid(r));

        /*
        function base36ToBigInt(str){
            return [...str].reduce((acc,curr) => BigInt(parseInt(curr, 36)) + BigInt(36) * acc, 0n);
        }
        function bigIntToBase36(num){
            return num.toString(36);
        }
        bigIntToBase36(BigInt("27131220722387717365029171224460129416")); // BigInt不带引号则必须用n结尾：0n
        base36ToBigInt(bigIntToBase36(BigInt("27131220722387717365029171224460129416")));
         */
        uuidString = "7fffffff-ffff-ffff-ffff-ffffffffffff";
        r = uuidToBase36(uuidString);
        System.out.println(r);
        System.out.println(base36ToUuid(r));

        //  max lenght = 25个
        var b36 = UuidUtils.uuidToBase36(new UUID(-1, -1).toString());
        System.out.println("36 - " + b36);
    }

    /*
      注意：
        var uuid = UUID.fromString("7fffffff-ffff-ffff-ffff-ffffffffffff");
        当 uuid.getMostSignificantBits() > Long.MAX_VALUE 或等于 -1 时；
        new BigInteger(1, bytes).toByteArray() 会在数组头部多出个 byte[0] = 0x0。
     */
    public static UUID bigIntegerToUUID(BigInteger bi) {
        var uuidBytes = bi.toByteArray();
        ByteBuffer bb = ByteBuffer.wrap(uuidBytes);
        if (uuidBytes.length > 16) {
            bb.get(); // note - 去除 new BigInteger(1, bytes).toByteArray() 多出的"00"前缀：
        }
        UUID uuid = new UUID(bb.getLong(), bb.getLong());
        return uuid;
    }

    public static byte[] uuidToUuidBytes(UUID uuid) {
        long hi = uuid.getMostSignificantBits();
        long lo = uuid.getLeastSignificantBits();
        byte[] bytes = ByteBuffer.allocate(16).putLong(hi).putLong(lo).array();
        return bytes;
    }

    public static UUID uuidBytesToUuid(byte[] uuidBytes) {
        ByteBuffer bb = ByteBuffer.wrap(uuidBytes);
        UUID uuid = new UUID(bb.getLong(), bb.getLong());
        return uuid;
    }

    public static String uuidToHex(UUID uuid) {
        var hex = HexFormat.of().formatHex(uuidToUuidBytes(uuid));

//        if (hex.length() > 32) { // 去除多出的"00"前缀：
//            //System.out.println(hex.length());
//            hex = new StringBuffer(hex).reverse().toString().substring(0, 32);
//            //System.out.println("hex hex - " + hex);
//            hex = new StringBuffer(hex).reverse().toString();
//        }
        //String r = uuid.toString().replaceAll("-", "");
        System.out.println("hex - " + hex);

        return hex;
    }

    public static BigInteger uuidToBigInteger(UUID uuid) {
        //UUID uuid = UUID.randomUUID();
        long hi = uuid.getMostSignificantBits();
        long lo = uuid.getLeastSignificantBits();
        byte[] bytes = ByteBuffer.allocate(16).putLong(hi).putLong(lo).array();
        BigInteger big = new BigInteger(1, bytes);
        return big;
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

    public static BigInteger randomUuidToBigInteger() {
        UUID uuid = UUID.randomUUID();
        return uuidToBigInteger(uuid);
    }

    public static BigInteger uuidStringToBigInteger(String uuidString) {
        return new BigInteger(uuidToHex(UUID.fromString(uuidString)), 16);
    }

    public static String uuid() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public static byte[] uuidStringToBytes(String uuidString) {
        return uuidStringToBigInteger(uuidString).toByteArray();
    }

    public static String uuidToBase32(String uuidString) {
        return Base32.encode(uuidStringToBytes(uuidString));
    }

    public static String base32ToUuid(String base32uuid) {
        byte[] byUuid = Base32.decode(base32uuid);
        ByteBuffer bb = ByteBuffer.wrap(byUuid);
        UUID uuid = new UUID(bb.getLong(), bb.getLong());
        return uuid.toString();
    }

    public static String uuidToBase36(String uuidString) {
        return ShortBase36.uuidStringToBase36(uuidString);
    }

    public static UUID base36ToUuid(String base36uuid) {
        var r = ShortBase36.base36ToBase10(base36uuid);
        var uuid = bigIntegerToUUID(r);
        System.out.println("uuid - " + uuid.toString());
        return uuid;
    }
}
