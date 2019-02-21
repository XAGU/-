package com.xiaolian.amigo.util;

import android.os.Build;

/**
 * @wcm
 * 系统版本工具类
 */
public class VersionUtils {

    /**
     * 判断是否是大于或者5.0版本
     * @return
     */
    public static boolean isUpLollipop(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            return true ;
        }
        else return false ;
    }}
