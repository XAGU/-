package com.xiaolian.amigo.util;

import android.content.Context;
import android.util.TypedValue;

/**
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
