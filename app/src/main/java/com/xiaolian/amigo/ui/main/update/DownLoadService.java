package com.xiaolian.amigo.ui.main.update;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.util.AppUtils;

import java.io.File;

/**
 * Created by adamzfc on 2017/3/29.
 */

public class DownLoadService extends Service {
    private static final int NOTIFICATION_ID = 0;
    private int notificationIcon;
    private String filePath;
    private boolean isBackground = false;
    private DownLoadTask mDownLoadTask;
    private DownLoadTask.ProgressListener mProgressListener;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;

    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    public void startDownLoad(String url) {
        filePath = AppUtils.getApkFilePath(getApplicationContext(),url);
        mDownLoadTask = new DownLoadTask(filePath, url, new DownLoadTask.ProgressListener() {
            @Override
            public void update(long bytesRead, long contentLength, boolean done) {

                if (done) {
                    mNotificationManager.cancel(NOTIFICATION_ID);
                }
                if (isBackground) {
                    if (done) {
                        startActivity(AppUtils.openApkFile(getApplicationContext(),new File(filePath)));
                    } else {
                        int currentProgress = (int) (bytesRead * 100 / contentLength);
                        if (currentProgress < 1) {
                            currentProgress = 1;
                        }
                        notification(currentProgress);
                    }
                    return;
                }
                if (mProgressListener != null) {
                    mProgressListener.update(bytesRead, contentLength, done);
                }
            }
        });
        mDownLoadTask.start();
    }

    public void setBackground(boolean background) {
        isBackground = background;
    }

    private final DownLoadBinder mDownLoadBinder = new DownLoadBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mDownLoadBinder;
    }

    public void cancel() {
        mDownLoadTask.cancel();
        mDownLoadTask.interrupt();
        mDownLoadTask = null;
    }

    public void setNotificationIcon(int notificationIcon) {
        this.notificationIcon = notificationIcon;
    }

    public class DownLoadBinder extends Binder {
        public DownLoadService getService() {
            return DownLoadService.this;
        }
    }

    public void registerProgressListener(DownLoadTask.ProgressListener progressListener) {
        mProgressListener = progressListener;
    }

    public void showNotification(int current) {
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("文件下载")
                .setContentText("正在下载中...")
                .setSmallIcon(notificationIcon == 0 ? R.mipmap.ic_launcher : notificationIcon);
        mBuilder.setProgress(100, current, false);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

    }

    private void notification(int current) {
        if (mBuilder == null) {
            showNotification(current);
            return;
        }
        mBuilder.setProgress(100, current, false);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}
