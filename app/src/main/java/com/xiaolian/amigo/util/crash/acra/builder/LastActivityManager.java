package com.xiaolian.amigo.util.crash.acra.builder;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.xiaolian.amigo.util.crash.acra.ACRA;
import com.xiaolian.amigo.util.crash.acra.dialog.BaseCrashReportDialog;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static  com.xiaolian.amigo.util.crash.acra.ACRA.LOG_TAG;

/**
 * Responsible for tracking the last Activity other than any CrashReport dialog that was created.
 *
 * @since 4.8.0
 */
public final class LastActivityManager {

    @NonNull
    private WeakReference<Activity> lastActivityCreated = new WeakReference<Activity>(null);
    private List<Activity> activities;

    public LastActivityManager(@NonNull Application application) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {

            // ActivityLifecycleCallback only available for API14+
            application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
                @Override
                public void onActivityCreated(@NonNull Activity activity, Bundle savedInstanceState) {
                    if (ACRA.DEV_LOGGING) ACRA.log.d(LOG_TAG, "onActivityCreated " + activity.getClass());
                    if (!(activity instanceof BaseCrashReportDialog)) {
                        // Ignore CrashReportDialog because we want the last
                        // application Activity that was started so that we can explicitly kill it off.
                        lastActivityCreated = new WeakReference<Activity>(activity);
                        addActivity(activity);
                    }
                }

                @Override
                public void onActivityStarted(@NonNull Activity activity) {
                    if (ACRA.DEV_LOGGING) ACRA.log.d(LOG_TAG, "onActivityStarted " + activity.getClass());
                }

                @Override
                public void onActivityResumed(@NonNull Activity activity) {
                    if (ACRA.DEV_LOGGING) ACRA.log.d(LOG_TAG, "onActivityResumed " + activity.getClass());
                }

                @Override
                public void onActivityPaused(@NonNull Activity activity) {
                    if (ACRA.DEV_LOGGING) ACRA.log.d(LOG_TAG, "onActivityPaused " + activity.getClass());
                }

                @Override
                public void onActivityStopped(@NonNull Activity activity) {
                    if (ACRA.DEV_LOGGING) ACRA.log.d(LOG_TAG, "onActivityStopped " + activity.getClass());
                    synchronized (this){
                        notify();
                    }
                }

                @Override
                public void onActivitySaveInstanceState(@NonNull Activity activity, Bundle outState) {
                    if (ACRA.DEV_LOGGING) ACRA.log.d(LOG_TAG, "onActivitySaveInstanceState " + activity.getClass());
                }

                @Override
                public void onActivityDestroyed(@NonNull Activity activity) {
                    if (ACRA.DEV_LOGGING) ACRA.log.d(LOG_TAG, "onActivityDestroyed " + activity.getClass());
                    removeActivity(activity);
                }
            });
        }
    }

    @Nullable
    public Activity getLastActivity() {
        return lastActivityCreated.get();
    }

    public void clearLastActivity() {
        lastActivityCreated.clear();
    }

    public void waitForActivityStop(int timeOutInMillis){
        synchronized (this) {
            try {
                wait(timeOutInMillis);
            } catch (InterruptedException ignored) {
            }
        }
    }

    /**
     * 移除Activity
     */
    public void removeActivity(Activity activity) {
        if (activities.contains(activity)) {
            activities.remove(activity);
        }

        if (activities.size() == 0) {
            activities = null;
        }
    }

    /**
     * 添加Activity
     */
    public void addActivity(Activity activity) {
        if (activities == null) {
            activities = new LinkedList<>();
        }

        if (!activities.contains(activity)) {
            activities.add(activity);//把当前Activity添加到集合中
        }
    }

    /**
     * 销毁所有activity
     */
    public void removeAllActivities() {
        for (Activity activity : activities) {
            if (null != activity) {
                activity.finish();
                activity.overridePendingTransition(0, 0);
            }
        }
    }
}
