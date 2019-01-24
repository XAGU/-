/*
 * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://mindorks.com/license/apache-v2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.xiaolian.amigo.util;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.network.model.version.VersionDialogTime;
import com.xiaolian.amigo.ui.main.MainActivity;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.ACTIVITY_SERVICE;
import static com.xiaolian.amigo.util.Constant.UPDATE_REMIND_INTERVAL;

/**
 * @author caidong
 * @date 17/9/14
 */
public final class CommonUtil {

    private static final String TAG = "CommonUtil";

    private CommonUtil() {
    }

    public static Dialog showLoadingDialog(Context context) {
//        Dialog progressDialog = new Dialog(context);
//        progressDialog.show();
////        if (progressDialog.getWindow() != null) {
////            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
////        }
//        progressDialog.setContentView(R.layout.progress_dialog);
//        PullToRefreshIndicator loading = (PullToRefreshIndicator) progressDialog.findViewById(R.id.loading);
//        loading.setIndicatorColor(0xffB5B5B5);
////        progressDialog.setIndeterminate(true);
//        progressDialog.setCancelable(false);
//        progressDialog.setCanceledOnTouchOutside(false);

        final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progress_dialog);
        dialog.setCancelable(false);
//        PullToRefreshIndicator loading = (PullToRefreshIndicator) dialog.findViewById(R.id.loading);
//        loading.setIndicatorColor(0xffB5B5B5);
        dialog.show();
        return dialog;
    }

    @SuppressLint("all")
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static boolean isEmailValid(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static String loadJSONFromAsset(Context context, String jsonFileName)
            throws IOException {

        AssetManager manager = context.getAssets();
        InputStream is = manager.open(jsonFileName);

        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();

        return new String(buffer, "UTF-8");
    }

    public static String getTimeStamp() {
        return new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(Long s) {
        if (s == null) {
            return "----";
        }
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.getDefault());
        Date date = new Date(s);
        res = simpleDateFormat.format(date);
        return res;
    }

    /**
     * 计算指定的 View 在屏幕中的坐标。
     */
    public static RectF calcViewScreenLocation(View view) {
        int[] location = new int[2];
        // 获取控件在屏幕中的位置，返回的数组分别为控件左顶点的 x、y 的值
        view.getLocationOnScreen(location);
        return new RectF(location[0], location[1], location[0] + view.getWidth(),
                location[1] + view.getHeight());
    }

    /**
     * 文本复制
     *
     * @param content 内容
     * @param context 上下文
     */
    public static void copy(@NonNull String content, Context context) {
        ClipboardManager cmb = (ClipboardManager) context
                .getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("simple label", content.trim());
        cmb.setPrimaryClip(clip);
    }

    /**
     * 调用拨号界面
     *
     * @param phone 电话号码
     */
    public static void call(Context context, String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 弹出软键盘
     *
     * @param context  上下文
     * @param editText 输入框
     */
    public static void showSoftInput(Context context, EditText editText) {
        if (context != null && editText != null) {
            editText.requestFocus();
            editText.postDelayed(() -> {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
                if (imm != null) {
                    imm.showSoftInput(editText, 0);
                }
            }, 200);
        }
    }

    public static void crash() {
        throw new NullPointerException();
    }

    public static String getMD5(String str) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getCaller() {
        StackTraceElement stack[] = new Throwable().getStackTrace();
        return stack[0].getMethodName();
    }

    public static void sendNotify(Context context, int id, String  notifyTitle, String title, String content,
                                  Class clz, Bundle bundle){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setTicker(notifyTitle);
        builder.setContentTitle(title);
        builder.setContentText(content);
        //ContentInfo 在通知的右侧 时间的下面 用来展示一些其他信息
        //builder.setContentInfo("2");
        //number设计用来显示同种通知的数量和ContentInfo的位置一样
        //如果设置了ContentInfo则number会被隐藏
        //builder.setNumber(2);
        builder.setAutoCancel(true);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.mipmap.ic_launcher));
        if (clz != null) {
            Intent intent = new Intent(context,clz);
            intent.putExtra(Constant.EXTRA_KEY, bundle);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pIntent = PendingIntent.getActivity(context,1,intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            //点击跳转的intent
            builder.setContentIntent(pIntent);
        } else {
            Intent intent = new Intent();
            PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
            builder.setContentIntent(pIntent);
        }
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        Notification notification = builder.build();
        NotificationManager manager= (NotificationManager) context.
                getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(id, notification);
    }



    public static String getRuningApp(Context context) {
        if (Build.VERSION.SDK_INT >= 21) {
            final int PROCESS_STATE_TOP = 2;
            ActivityManager.RunningAppProcessInfo currentInfo = null;
            Field field = null;
            try {
                field = ActivityManager.RunningAppProcessInfo.class.getDeclaredField("processState");
            } catch (Exception ignored) {
            }
            ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> appList = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo app : appList) {
                if (app.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                        && app.importanceReasonCode == ActivityManager.RunningAppProcessInfo.REASON_UNKNOWN) {
                    Integer state = null;
                    try {
                        state = field.getInt(app);
                    } catch (Exception e) {
                    }
                    if (state != null && state == PROCESS_STATE_TOP) {
                        currentInfo = app;
                        break;
                    }
                }
            }
            if (currentInfo != null) {
                System.out.println(currentInfo.processName);
                return currentInfo.processName;
            }
            return null;
        } else {
            try {
                ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
                ActivityManager.RunningTaskInfo foregroundTaskInfo = am.getRunningTasks(1).get(0);
                return foregroundTaskInfo.topActivity.getPackageName();
            } catch (Exception e) {
                return null;
            }
        }
    }


    /**
     * 判断是否显示版本更新弹窗
     * @param versionDialogTime  存储的用mobile 为key  ,time 为value 的map
     * @param mobile  手机号
     * @return  true   显示  false  不显示
     */
    public static boolean canShowUpdateDialog(VersionDialogTime versionDialogTime , String mobile){
        if (versionDialogTime == null) return true ;

        Map<String ,Long> updateTime = versionDialogTime.getVersionDialogTime();

        if (updateTime == null || updateTime.isEmpty()) return true ;

        if (updateTime.size() == 1 && updateTime.containsKey("")) return false ;
        if (updateTime.containsKey(mobile)){
            if (System.currentTimeMillis() - updateTime.get(mobile) >=UPDATE_REMIND_INTERVAL) return  true;
            else return false ;
        }
        return true ;
    }

}
