/*
 * Copyright (c) 2016
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xiaolian.amigo.util.crash.acra.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.xiaolian.amigo.ui.main.SplashActivity;
import com.xiaolian.amigo.util.Log;
import com.xiaolian.amigo.util.crash.acra.ACRA;
import com.xiaolian.amigo.util.crash.acra.builder.LastActivityManager;
import com.xiaolian.amigo.util.crash.acra.config.ACRAConfiguration;
import com.xiaolian.amigo.util.crash.acra.sender.SenderService;

import java.util.List;

import static  com.xiaolian.amigo.util.crash.acra.ACRA.LOG_TAG;

/**
 * Takes care of cleaning up a process and killing it.
 *
 * @author F43nd1r
 * @since 4.9.2
 */

public final class ProcessFinisher {
    private final Context context;
    private final ACRAConfiguration config;
    private final LastActivityManager lastActivityManager;

    public ProcessFinisher(@NonNull Context context, @NonNull ACRAConfiguration config, @NonNull LastActivityManager lastActivityManager) {
        this.context = context;
        this.config = config;
        this.lastActivityManager = lastActivityManager;
    }

    public void endApplication(@Nullable Thread uncaughtExceptionThread) {
        finishLastActivity(uncaughtExceptionThread);
        lastActivityManager.removeAllActivities();
        stopServices();
        killProcessAndExit();
    }

    public void finishLastActivity(@Nullable Thread uncaughtExceptionThread) {
        // Trying to solve https://github.com/ACRA/acra/issues/42#issuecomment-12134144
        // Determine the current/last Activity that was started and close
        // it. Activity#finish (and maybe it's parent too).
        final Activity lastActivity = lastActivityManager.getLastActivity();
        if (lastActivity != null) {
            if (ACRA.DEV_LOGGING)
                ACRA.log.d(LOG_TAG, "Finishing the last Activity prior to killing the Process");
            lastActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    lastActivity.finish();
                    if (ACRA.DEV_LOGGING)
                        ACRA.log.d(LOG_TAG, "Finished " + lastActivity.getClass());
                }
            });

            // A crashed activity won't continue its lifecycle. So we only wait if something else crashed
            if (uncaughtExceptionThread != lastActivity.getMainLooper().getThread()) {
                lastActivityManager.waitForActivityStop(100);
            }
            lastActivityManager.clearLastActivity();
        }
    }

    private void stopServices() {
        if (config.stopServicesOnCrash()) {
            final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            final List<ActivityManager.RunningServiceInfo> runningServices = activityManager.getRunningServices(Integer.MAX_VALUE);
            final int pid = Process.myPid();
            for (ActivityManager.RunningServiceInfo serviceInfo : runningServices) {
                if (serviceInfo.pid == pid && !SenderService.class.getName().equals(serviceInfo.service.getClassName())) {
                    try {
                        final Intent intent = new Intent();
                        intent.setComponent(serviceInfo.service);
                        context.stopService(intent);
                    } catch (SecurityException e) {
                        if (ACRA.DEV_LOGGING)
                            ACRA.log.d(ACRA.LOG_TAG, "Unable to stop Service " + serviceInfo.service.getClassName() + ". Permission denied");
                    }
                }
            }
        }
    }

    private void killProcessAndExit() {
        //利用系统时钟进行重启任务
        AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        try {
            Intent intent = new Intent(context, SplashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent restartIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 10, restartIntent); // x秒钟后重启应用
        } catch (Exception e) {
            Log.e("ACRA", "first class error:" + e);
        }
        Process.killProcess(Process.myPid());
//        System.exit(10);
        System.exit(1);
        System.gc();
    }
}
