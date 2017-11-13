package com.xiaolian.amigo.util;

import android.annotation.SuppressLint;
import android.content.Context;

import com.umeng.analytics.MobclickAgent;

/**
 * <p>
 * Created by zcd on 17/11/10.
 */

public class Log {
    public static final boolean DEBUG = true;
    public static final boolean UMENG = true;
    private static final int ZERO = 0;
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        Log.context = context;
    }

    public static int v(String tag, String msg) {
        if (DEBUG) {
            return android.util.Log.v(tag, msg);
        }
        return ZERO;
    }

    public static int v(String tag, String msg, Throwable tr) {
        if (DEBUG) {
            return android.util.Log.v(tag, msg, tr);
        }
        return ZERO;
    }

    public static int d(String tag, String msg) {
        if (DEBUG) {
            return android.util.Log.d(tag, msg);
        }
        return ZERO;
    }

    public static int d(String tag, String msg, Throwable tr) {
        if (DEBUG) {
            return android.util.Log.d(tag, msg, tr);
        }
        return ZERO;
    }

    public static int i(String tag, String msg) {
        if (DEBUG) {
            return android.util.Log.i(tag, msg);
        }
        return ZERO;
    }

    public static int i(String tag, String msg, Throwable tr) {
        if (DEBUG) {
            return android.util.Log.i(tag, msg, tr);
        }
        return ZERO;
    }

    public static int w(String tag, String msg) {
        if (DEBUG) {
            return android.util.Log.w(tag, msg);
        }
        return ZERO;
    }

    public static int w(String tag, String msg, Throwable tr) {
        if (DEBUG) {
            return android.util.Log.w(tag, msg, tr);
        }
        return ZERO;
    }

    public static int w(String tag, Throwable tr) {
        if (DEBUG) {
            return android.util.Log.w(tag, tr);
        }
        return ZERO;
    }

    public static int e(String tag, String msg) {
        if (DEBUG) {
            return android.util.Log.e(tag, msg);
        }
        if (UMENG) {
            MobclickAgent.reportError(getContext(), "customReport tag:" + tag + " msg:" + msg);
        }
        return ZERO;
    }

    public static int e(String tag, String msg, Throwable tr) {
        if (DEBUG) {
            return android.util.Log.e(tag, msg, tr);
        }
        if (UMENG) {
            MobclickAgent.reportError(getContext(), "customReport tag:" + tag + " msg:" + msg + " tr:" + tr.getMessage());
        }
        return ZERO;
    }

    public static int wtf(String tag, String msg) {
        if (DEBUG) {
            return android.util.Log.wtf(tag, msg);
        }
        if (UMENG) {
            MobclickAgent.reportError(getContext(), "customReport tag:" + tag
                    + " msg:" + msg);
        }
        return ZERO;
    }

    public static int wtf(String tag, Throwable tr) {
        if (DEBUG) {
            return android.util.Log.wtf(tag, tr);
        }
        if (UMENG) {
            MobclickAgent.reportError(getContext(), "customReport tag:" + tag + "tr:" + tr.getMessage());
        }
        return ZERO;
    }

    public static int wtf(String tag, String msg, Throwable tr) {
        if (DEBUG) {
            return android.util.Log.wtf(tag, msg, tr);
        }
        if (UMENG) {
            MobclickAgent.reportError(getContext(), "customReport tag:" + tag + " msg:" + msg + " tr:" + tr.getMessage());
        }
        return ZERO;
    }
}
