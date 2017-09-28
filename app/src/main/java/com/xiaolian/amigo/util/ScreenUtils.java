package com.xiaolian.amigo.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * 屏幕相关工具类
 * @author zcd
 */

public class ScreenUtils {
    private ScreenUtils() {
        throw new AssertionError();
    }

    public static float dpToPx(Context context, float dp) {
        if (context == null) {
            return -1;
        }
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public static float pxToDp(Context context, float px) {
        if (context == null) {
            return -1;
        }
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static int dpToPxInt(Context context, float dp) {
        return (int)(dpToPx(context, dp) + 0.5f);
    }

    public static int pxToDpCeilInt(Context context, float px) {
        return (int)(pxToDp(context, px) + 0.5f);
    }

    public static int getScreenWidth(Context context){
        return getScreenWidth(context,false);
    }

    public static int getScreenHeight(Context context){
        return getScreenHeight(context,false);
    }

    /**
     *
     * @param context context
     * @param isPortrait 是否
     * @return 屏幕宽度
     */
    public static int getScreenWidth(Context context, boolean isPortrait){
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        int w=dm.widthPixels;
        int h=dm.heightPixels;
        if (isPortrait) {
            return w<h?w:h;
        }else{
            return w;
        }
    }

    public static int getScreenHeight(Context context, boolean isPortrait){
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        int w=dm.widthPixels;
        int h=dm.heightPixels;
        if (isPortrait) {
            return h>w&&isPortrait?h:w;
        }else{
            return h;
        }
    }
}
