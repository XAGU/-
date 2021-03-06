package com.xiaolian.amigo.ui.device.bathroom;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.base.WebActivity;
import com.xiaolian.amigo.ui.device.WaterDeviceBaseActivity;
import com.xiaolian.amigo.ui.device.intf.heator.IHeaterPresenter;
import com.xiaolian.amigo.ui.device.intf.heator.IHeaterView;
import com.xiaolian.amigo.util.AppUtils;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.H5StartUtils;

import javax.inject.Inject;

public class BathIngActivity extends WaterDeviceBaseActivity<IHeaterPresenter> implements IHeaterView {

    @Inject
    IHeaterPresenter<IHeaterView> presenter;

    @Inject
    H5StartUtils h5StartUtils ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
        presenter.onAttach(this);
    }

    @Override
    protected int setHeaderBackgroundDrawable() {
        return R.drawable.bg_rect_red;
    }

    @Override
    protected int setBottomBackgroundColor() {
        return R.color.heater_gradient_start;
    }

    @Override
    protected String setSubtitleString() {
        return getString(R.string.change_dormitory);
    }

    @Override
    protected View.OnClickListener setTitleClickListener() {
        return v -> changeDormitory();
    }

    @Override
    protected View.OnClickListener setTopRightIconClickListener() {
        return v -> h5StartUtils.startH5Service();
    }

    @Override
    protected int setTopRightIconDrawable() {
        return R.drawable.ic_question;
    }

    @Override
    protected int setHeaderIconDrawable() {
        return R.drawable.ic_shower;
    }

    @Override
    protected String getSlideButtonText() {
        return getString(R.string.comma_start_shower);
    }

    @Override
    protected String getBalanceTradeTip() {
        return getString(R.string.balance_trade_tip_water);
    }

    @Override
    public IHeaterPresenter setPresenter() {
        return presenter;
    }

    @Override
    public void showGuide() {
        showAlertNotice((dialog, isNotRemind) -> {
            dialog.dismiss();
            if (isNotRemind) {
                presenter.notShowRemindAlert();
            }
        });
    }

    @Override
    public String getAppVersion() {
         return AppUtils.getVersionName(this);
    }
}
