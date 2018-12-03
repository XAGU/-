package com.xiaolian.amigo.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.xiaolian.amigo.util.Log;

import java.io.File;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * App相关工具
 *
 * @author zcd
 * @date 17/10/16
 */

public class AppUtils {
    private static final String TAG = AppUtils.class.getSimpleName();

    private AppUtils() {
    }

    /**
     * 获取版本号
     *
     * @param context context
     * @return 当前应用的版本号
     */
    public static String getVersionName(Context context) {
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

    /**
     * 安装apk
     *
     * @param activity    activity
     * @param file        file
     * @param authority   authority
     * @param requestCode requestCode
     */
    public static void installApp(final Activity activity, final File file, final String authority, final int requestCode) {
        if (!FileUtils.isFileExists(file)) return;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data;
        String type = "application/vnd.android.package-archive";
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            data = Uri.fromFile(file);
        } else {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            data = FileProvider.getUriForFile(activity.getApplicationContext(), authority, file);
        }
        intent.setDataAndType(data, type);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivityForResult(intent, requestCode);
    }

    public static String getApkFilePath(Context context, String downLoadUrl) {
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/xiaolian/";
        File path = new File(filePath);
        if (!path.exists()) {
            path.mkdirs();
        }
        String fileName;
        if (downLoadUrl.endsWith(".apk")) {
            int index = downLoadUrl.lastIndexOf("/");
            if (index != -1) {
                fileName = downLoadUrl.substring(index);
            } else {
                fileName = context.getPackageName() + ".apk";
            }
        } else {
            fileName = context.getPackageName() + ".apk";
        }

        File file = new File(filePath, fileName);
        return file.getAbsolutePath();
    }

    public static Intent openApkFile(Context context, File outputFile) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        Uri uri;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            uri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", outputFile);
        } else {
            uri = Uri.fromFile(outputFile);
        }

        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        return intent;
    }

    /**
     * get application's versionCode
     *
     * @param context context
     * @return versionCode or -1
     */
    public static int getAppVersionCode(@NonNull Context context) {
        return getAppVersionCode(context, context.getPackageName());
    }

    /**
     * get application's versionCode
     *
     * @param context     context
     * @param packageName packageName
     * @return versionCode or -1
     */
    public static int getAppVersionCode(@NonNull Context context, @Nullable String packageName) {
        if (TextUtils.isEmpty(packageName)) return -1;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? -1 : pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }


    /**
     * The IMEI: 仅仅只对Android手机有效
     * 采用此种方法，需要在AndroidManifest.xml中加入一个许可：android.permission.READ_PHONE_STATE，并且用
     * 户应当允许安装此应用。作为手机来讲，IMEI是唯一的，它应该类似于 359881030314356（除非你有一个没有量产的手
     * 机（水货）它可能有无效的IMEI，如：0000000000000）。
     *
     * @return imei
     */
    public String getIMEI(Context mContext) {
        TelephonyManager TelephonyMgr = (TelephonyManager) mContext.getApplicationContext().getSystemService(TELEPHONY_SERVICE);
        String szImei = TelephonyMgr.getDeviceId();
        return szImei;
    }
}
