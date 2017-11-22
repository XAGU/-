package com.xiaolian.amigo.ui.main.update;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.util.AppUtils;

import java.io.File;

/**
 * 下载
 * Created by adamzfc on 2017/3/29.
 */

public class DownLoadDialog extends DialogFragment implements View.OnClickListener {
    private static final String TAG = "DownLoadDialog";
    private static final String TITLE_FORMAT = "正在下载（%s/%s）";
    private String mDownloadUrl;
    private int notificationIcon;
    private int currentProgress;
    private DownLoadService mDownLoadService;
    private IVersionModel mModel;
    private TextView mTvOk;

    public static DownLoadDialog newInstance(IVersionModel model, String downLoadUrl, int notificationIcon) {

        Bundle args = new Bundle();
        args.putString(IntentKey.URL, downLoadUrl);
        args.putInt(IntentKey.NOTIFICATION_ICON,notificationIcon);
        args.putSerializable(IntentKey.MODEL, model);
        DownLoadDialog fragment = new DownLoadDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_download, container, false);
        mDownloadUrl = getArguments().getString(IntentKey.URL);
        notificationIcon = getArguments().getInt(IntentKey.NOTIFICATION_ICON);
        mModel = (IVersionModel) getArguments().getSerializable(IntentKey.MODEL);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setContent(view, R.id.tv_update_content);
        setTitle(view, R.id.tv_update_title);

        mTvOk = (TextView) view.findViewById(R.id.tv_ok);
        mTvOk.setOnClickListener(this);
        mTvOk.setEnabled(false);

        Intent intent = new Intent(getActivity(), DownLoadService.class);
        getActivity().bindService(intent, mConnection , Context.BIND_AUTO_CREATE);
    }

    protected void setContent(View view, int contentId) {
        TextView tvContext = (TextView) view.findViewById(contentId);
        tvContext.setText(getContent());
    }

    private String getContent() {
        StringBuilder sb = new StringBuilder();
        sb.append(mModel.getVersionDescription());
        return sb.toString();
    }

    private String getTitle() {
        StringBuilder sb = new StringBuilder();
        sb.append("发现新版本")
                .append(mModel.getVersionName());
        return sb.toString();
    }

    private void setTitle(View view, int titleId) {
        TextView tvTitle = (TextView) view.findViewById(titleId);
        tvTitle.setText(getTitle());
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            DownLoadService.DownLoadBinder binder = (DownLoadService.DownLoadBinder) service;
            mDownLoadService = binder.getService();
            mDownLoadService.registerProgressListener(mProgressListener);
            mDownLoadService.startDownLoad(mDownloadUrl);
            mDownLoadService.setNotificationIcon(notificationIcon);

            if (!mModel.isMustUpdate()) {
                doBackground();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mDownLoadService = null;
        }
    };

    private DownLoadTask.ProgressListener mProgressListener = new DownLoadTask.ProgressListener() {
        @Override
        public void update(final long bytesRead, final long contentLength, final boolean done) {

            currentProgress = (int) (bytesRead * 100 / contentLength);
            if (currentProgress < 1) {
                currentProgress = 1;
            }
            Message message = mHandler.obtainMessage();
            message.what = done ? DONE : LOADING;
            message.arg1 = currentProgress;
            Bundle bundle = new Bundle();
            bundle.putLong("bytesRead", bytesRead);
            bundle.putLong("contentLength", contentLength);
            message.setData(bundle);
            message.sendToTarget();
        }
    };

    @Override
    public void onClick(View view) {
//        int id = view.getId();
//        if (id == R.id.btnCancel) {
//            doCancel();
//        } else if (id == R.id.btnBackground) {
//            doBackground();
//        }
        if (view.getId() == R.id.tv_ok) {
            getActivity().startActivity(AppUtils.openApkFile(getActivity(),
                    new File(AppUtils.getApkFilePath(getActivity(),mDownloadUrl))));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unbindService(mConnection);
    }

    private void doCancel() {
        mDownLoadService.cancel();
        getActivity().finish();
        Toast.makeText(getActivity().getApplicationContext(), "已取消更新", Toast.LENGTH_SHORT).show();
    }

    private void doBackground() {
        mDownLoadService.setBackground(true);
        mDownLoadService.showNotification(currentProgress);
        getActivity().finish();
        Toast.makeText(getActivity().getApplicationContext(), "正在后台进行更新", Toast.LENGTH_SHORT).show();
    }

    private final static int LOADING = 1000;
    private final static int DONE = 1001;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOADING:
                    mTvOk.setText("正在更新..." + msg.arg1 + "%");
//                    Bundle bundle = msg.getData();
//                    long bytesRead = bundle.getLong("bytesRead");
//                    long contentLength = bundle.getLong("contentLength");
//                    mTvTitle.setText(String.format(TITLE_FORMAT,
//                            Formatter.formatFileSize(getActivity().getApplication(),bytesRead),
//                            Formatter.formatFileSize(getActivity().getApplication(),contentLength)));
                    break;
                case DONE:
                    mTvOk.setText("立即安装");
                    mTvOk.setEnabled(true);
                    getActivity().startActivity(AppUtils.openApkFile(getActivity(),
                            new File(AppUtils.getApkFilePath(getActivity(),mDownloadUrl))));
//                    getActivity().finish();
                    Toast.makeText(getActivity(), "下载完成", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}
