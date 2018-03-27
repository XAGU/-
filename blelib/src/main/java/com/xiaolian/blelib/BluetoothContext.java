package com.xiaolian.blelib;

import android.content.Context;

/**
 * @author zcd
 * @date 18/3/27
 */

public class BluetoothContext {
    private static Context mContext;

    public static void set(Context context) {
        mContext = mContext;
    }

    public static Context get() {
        return mContext;
    }
}
