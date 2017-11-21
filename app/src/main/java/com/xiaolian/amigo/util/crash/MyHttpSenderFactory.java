package com.xiaolian.amigo.util.crash;

import android.content.Context;
import android.support.annotation.NonNull;

import com.xiaolian.amigo.util.crash.acra.ReportField;
import com.xiaolian.amigo.util.crash.acra.config.ACRAConfiguration;
import com.xiaolian.amigo.util.crash.acra.sender.HttpSender;
import com.xiaolian.amigo.util.crash.acra.sender.HttpSenderFactory;
import com.xiaolian.amigo.util.crash.acra.sender.ReportSender;
import com.xiaolian.amigo.util.crash.acra.sender.ReportSenderFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Created by zcd on 17/11/21.
 */

public class MyHttpSenderFactory implements ReportSenderFactory {
    @NonNull
    @Override
    public ReportSender create(@NonNull Context context, @NonNull ACRAConfiguration config) {
        Map<ReportField, String> map = new HashMap<>();
        map.put(ReportField.REPORT_ID, "reportId");
        map.put(ReportField.DEVICE_ID, "deviceId");
        map.put(ReportField.APP_VERSION_CODE, "appVersionCode");
        map.put(ReportField.APP_VERSION_NAME, "appVersionName");
        map.put(ReportField.PACKAGE_NAME, "packageName");
        map.put(ReportField.PHONE_MODEL, "phoneModel");
        map.put(ReportField.ANDROID_VERSION, "androidVersion");
        map.put(ReportField.BRAND, "brand");
        map.put(ReportField.PRODUCT, "product");
        map.put(ReportField.TOTAL_MEM_SIZE, "totalMemSize");
        map.put(ReportField.AVAILABLE_MEM_SIZE, "availableMemSize");
        map.put(ReportField.BUILD_CONFIG, "buildConfig");
        map.put(ReportField.INITIAL_CONFIGURATION, "initialConfiguration");
        map.put(ReportField.STACK_TRACE, "stackTrace");
        map.put(ReportField.CRASH_CONFIGURATION, "crashConfiguration");
        map.put(ReportField.DISPLAY, "display");
        map.put(ReportField.DROPBOX, "dropBox");
        map.put(ReportField.LOGCAT, "locat");
        map.put(ReportField.DEVICE_FEATURES, "deviceFeatures");
        map.put(ReportField.EVENTSLOG, "eventSlog");
        map.put(ReportField.SHARED_PREFERENCES, "sharedPreferences");
        map.put(ReportField.SETTINGS_GLOBAL, "settingsGlobal");
        map.put(ReportField.SETTINGS_SECURE, "settingsSecure");
        map.put(ReportField.SETTINGS_SYSTEM, "settingsSystem");
        map.put(ReportField.SIGNATURE, "signature");
        return new HttpSender(config, config.httpMethod(), config.reportType(), map);
    }
}
