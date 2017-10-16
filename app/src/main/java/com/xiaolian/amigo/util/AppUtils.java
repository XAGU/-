package com.xiaolian.amigo.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * App相关工具
 * <p>
 * Created by zcd on 10/16/17.
 */

public class AppUtils {
    private static final String TAG = AppUtils.class.getSimpleName();
    private AppUtils(){
    }

    /**
     * 获取版本号
     * @param context context
     * @return 当前应用的版本号
     */
    public static String getVersion(Context context) {
        try {
            PackageManager packageManager = context.getApplicationContext().getPackageManager();
            String packageName = context.getApplicationContext().getPackageName();
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.wtf(TAG, e);
            return "未知版本号";
        }
    }
}
