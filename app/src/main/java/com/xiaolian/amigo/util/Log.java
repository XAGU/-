package com.xiaolian.amigo.util;

/**
 * <p>
 * Created by zcd on 17/11/10.
 */

public class Log {
    public static final boolean DEBUG = true;
    private static final int ZERO = 0;

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

//    public static int w(String tag, Throwable tr) {
//        return printlns(LOG_ID_MAIN, WARN, tag, "", tr);
//    }
//
//    public static int e(String tag, String msg) {
//        return println_native(LOG_ID_MAIN, ERROR, tag, msg);
//    }
//    public static int e(String tag, String msg, Throwable tr) {
//        return printlns(LOG_ID_MAIN, ERROR, tag, msg, tr);
//    }
//
//    public static int wtf(String tag, String msg) {
//        return wtf(LOG_ID_MAIN, tag, msg, null, false, false);
//    }
//
//    public static int wtfStack(String tag, String msg) {
//        return wtf(LOG_ID_MAIN, tag, msg, null, true, false);
//    }
//
//    public static int wtf(String tag, Throwable tr) {
//        return wtf(LOG_ID_MAIN, tag, tr.getMessage(), tr, false, false);
//    }
//
//    public static int wtf(String tag, String msg, Throwable tr) {
//        return wtf(LOG_ID_MAIN, tag, msg, tr, false, false);
//    }
//
//    static int wtf(int logId, String tag, String msg, Throwable tr, boolean localStack,
//                   boolean system) {
//        TerribleFailure what = new TerribleFailure(msg, tr);
//        // Only mark this as ERROR, do not use ASSERT since that should be
//        // reserved for cases where the system is guaranteed to abort.
//        // The onTerribleFailure call does not always cause a crash.
//        int bytes = printlns(logId, ERROR, tag, msg, localStack ? what : tr);
//        sWtfHandler.onTerribleFailure(tag, what, system);
//        return bytes;
//    }

}
