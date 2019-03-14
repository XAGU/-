package com.xiaolian.amigo.util;


public class StringUtils {

    /**
     * 用';'来拼接token,accessToken在前，refreshToken在后
     * @param accessToken
     * @param refreshToken
     * @return
     */
    public static String appendToken(String accessToken , String refreshToken){
        return accessToken +Constant.TOKEN_SEPARATOR + refreshToken ;
    }
}
