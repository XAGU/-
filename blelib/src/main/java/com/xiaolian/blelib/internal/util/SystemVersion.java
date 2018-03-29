package com.xiaolian.blelib.internal.util;

import android.os.Build;

/**
 * @author zcd
 * @date 18/3/28
 */

public class SystemVersion {
    public static boolean isMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }
}
