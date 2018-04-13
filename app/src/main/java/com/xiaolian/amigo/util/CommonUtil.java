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
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.RectF;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.xiaolian.amigo.R;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

}
