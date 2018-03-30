package com.xiaolian.blelib.internal.util;

import android.os.Build;
import android.support.annotation.RestrictTo;

/**
 * @author zcd
 * @date 18/3/28
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public class SystemVersion {
    public static boolean isMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }
}
