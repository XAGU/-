package com.xiaolian.amigo.ui.more;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tbruyelle.rxpermissions.RxPermissions;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.network.model.version.VersionDTO;
import com.xiaolian.amigo.ui.main.update.IntentKey;
import com.xiaolian.amigo.ui.main.update.UpdateActivity;
import com.xiaolian.amigo.ui.more.intf.IAboutUsPresenter;
import com.xiaolian.amigo.ui.more.intf.IAboutUsView;
import com.xiaolian.amigo.ui.widget.dialog.ChangeHostDialog;
import com.xiaolian.amigo.util.AppUtils;
import com.xiaolian.amigo.util.CommonUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

/**
 * 关于我们
 *
 * @author zcd
 * @date 17/10/13
 */

public class AboutUsActivity extends MoreBaseActivity implements IAboutUsView {
    @Inject
    IAboutUsPresenter<IAboutUsView> presenter;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.rl_update)
    RelativeLayout rlUpdate;
    @BindView(R.id.tv_new_version)
    TextView tvNewVersion;
    private RxPermissions rxPermissions;
    private String versionName;
    private Integer versionCode;
    private VersionDTO model;
    private ChangeHostDialog hostDialog;

    @Override
    protected void initView() {
        setUnBinder(ButterKnife.bind(this));
        setMainBackground(R.color.colorBackgroundWhite);
        getActivityComponent().inject(this);
        presenter.onAttach(AboutUsActivity.this);
        versionName = AppUtils.getVersionName(getApplicationContext());
        versionCode = AppUtils.getAppVersionCode(getApplicationContext());
        tvVersion.setText(getString(R.string.app_name) + " " + versionName);
        presenter.checkUpdate(versionCode, versionName, false);
    }

    @Override
    protected int setTitle() {
        return R.string.about_us;
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_about_us;
    }

    @Override
    public void showUpdateDialog(VersionDTO model) {
        if (rxPermissions == null) {
            rxPermissions = RxPermissions.getInstance(this);
        }
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) {
                        Intent intent = new Intent(this, UpdateActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra(IntentKey.MODEL, model);
                        intent.putExtra(IntentKey.NOTIFICATION_ICON, R.mipmap.ic_launcher);
                        startActivity(intent);
                    } else {
                        showMessage("没有SD卡权限");
                    }
                });
    }

    @Override
    public void showUpdateButton(VersionDTO version) {
        if (version == null) {
            return;
        }
        model = version;
        rlUpdate.setVisibility(View.VISIBLE);
        tvNewVersion.setText("V" + version.getVersionName());
    }

    @OnClick(R.id.rl_update)
    void onUpdateClick() {
        showUpdateDialog(model);
    }

    @OnClick(R.id.rl_attention)
    void onAttentionClick() {
        CommonUtil.copy("笑联", this);
        onSuccess("复制成功");
    }

    @OnLongClick(R.id.iv_logo)
    boolean changeHost() {
        if (hostDialog == null) {
            hostDialog = new ChangeHostDialog(this);
            hostDialog.setOnOkClickListener((dialog, host, host2) -> presenter.changeHost(host, host2));
        }
        hostDialog.show();
        return true;
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }
}
