package com.xiaolian.amigo.ui.main.update;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import com.xiaolian.amigo.R;

public class UpdateActivity extends AppCompatActivity {
    private int notificationIcon;
    protected IVersionModel mModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setFinishOnTouchOutside(false);
        notificationIcon = getIntent().getIntExtra(IntentKey.NOTIFICATION_ICON, 0);
        mModel = (IVersionModel) getIntent().getSerializableExtra(IntentKey.MODEL);
        if (mModel == null) {
            finish();
            return;
        }

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, getUpdateDialogFragment())
                .commit();

    }

    public void showDownLoadProgress() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, getDownLoadDialogFragment())
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (mModel.isMustUpdate()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void finish() {
        setResult(RESULT_OK);
        super.finish();
    }

    protected Fragment getUpdateDialogFragment() {
        return UpdateDialog.newInstance(mModel);
    }

    protected Fragment getDownLoadDialogFragment() {
        return DownLoadDialog.newInstance(mModel, mModel.getDownloadUrl(),notificationIcon);
    }
}
