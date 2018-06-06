package com.xiaolian.amigo.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author zcd
 * @date 18/6/5
 */
public class MD5Util {

    private static ThreadLocal<MessageDigest> MD = new ThreadLocal<>();

    public static String md5(String str) {
        final byte[] byteDigest = getMessageDigest().digest(str.getBytes());
        int i;
        StringBuilder buf = new StringBuilder("");
        for (byte aByteDigest : byteDigest) {
            i = aByteDigest;
            if (i < 0) {
                i += 256;
            }
            if (i < 16) {
                buf.append("0");
            }
            buf.append(Integer.toHexString(i));
        }
        return buf.toString();
    }

    private static MessageDigest getMessageDigest() {
        MessageDigest digest = MD.get();
        if (digest != null) {
            return digest;
        }
        try {
            digest = MessageDigest.getInstance("MD5");
            MD.set(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return digest;
    }

    private MD5Util() {
    }

    public static void main(String[] args) {
        System.out.println(MD5Util.md5("uploadmemberinfo" + "1234567" + "2018-01-15 14:00:01" + "KqZ5LmlspWd1nsIMTlGd48lA6zVHO@ZY"));
        System.out.println(MD5Util.md5("uploadchecklog" + "1234567" + "2018-01-15 14:00:01" + "KqZ5LmlspWd1nsIMTlGd48lA6zVHO@ZY"));
        System.out.println(MD5Util.md5("32082619981111066120180323175450"));
    }
}
