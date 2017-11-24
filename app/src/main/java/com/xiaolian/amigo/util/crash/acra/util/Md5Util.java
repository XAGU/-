package com.xiaolian.amigo.util.crash.acra.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * <p>
 * Created by zcd on 17/11/23.
 */

public class Md5Util {
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

    private static MessageDigest getMessageDigest(){
        MessageDigest digest = MD.get();
        if( digest != null ){
            return digest;
        }
        try{
            digest = MessageDigest.getInstance("MD5");
            MD.set(digest);
        }
        catch(NoSuchAlgorithmException e){
            throw new RuntimeException(e);
        }
        return digest;
    }

    private Md5Util(){}
}
