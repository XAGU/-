package com.xiaolian.amigo.util;

import android.view.MotionEvent;
import android.view.View;

/**
 * @author zcd
 * @date 17/11/6
 */

public class TouchUtils {
    private TouchUtils() {
    }

    public static boolean isTouchOutsideInitialPosition(MotionEvent event, View view) {
        return event.getX() > view.getX() + view.getWidth();
    }
}
