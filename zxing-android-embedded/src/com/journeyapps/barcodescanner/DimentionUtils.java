package com.journeyapps.barcodescanner;

import android.content.Context;
import android.util.TypedValue;

/**
 * 尺寸相关工具类
 *
 * @author zcd
 * @date 17/11/6
 */

public class DimentionUtils {
    private DimentionUtils() {
    }

    public static float converPixelsToSp(float px, Context context) {
        return px / context.getResources().getDisplayMetrics().scaledDensity;
    }

    public static int convertSpToPixels(float sp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }
}
