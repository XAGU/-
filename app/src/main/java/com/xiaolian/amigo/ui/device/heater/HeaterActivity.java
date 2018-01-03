package com.xiaolian.amigo.ui.device.heater;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.TradeStep;
import com.xiaolian.amigo.ui.base.WebActivity;
import com.xiaolian.amigo.ui.device.WaterDeviceBaseActivity;
import com.xiaolian.amigo.ui.device.intf.heator.IHeaterPresenter;
import com.xiaolian.amigo.ui.device.intf.heator.IHeaterView;
import com.xiaolian.amigo.ui.user.ChooseDormitoryActivity;
import com.xiaolian.amigo.ui.widget.dialog.GuideDialog;
import com.xiaolian.amigo.util.Constant;

import javax.inject.Inject;

/**
 * 热水澡设备页
 *
 * @author zcd
 */

public class HeaterActivity extends WaterDeviceBaseActivity<IHeaterPresenter> implements IHeaterView {
    @Inject
    IHeaterPresenter<IHeaterView> presenter;

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
        return v -> startActivity(new Intent(getApplicationContext(), WebActivity.class)
                .putExtra(WebActivity.INTENT_KEY_URL, Constant.H5_HELP));
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
    public IHeaterPresenter setPresenter() {
        return presenter;
    }

    @Override
    public void showGuide() {
        // 不显示引导页
        // 显示引导页
//        GuideDialog guideDialog = new GuideDialog(this, GuideDialog.TYPE_HEATER);
//        guideDialog.setLocation(getLocation());
//        guideDialog.show();
        showAlertNotice((dialog, isNotRemind) -> {
            dialog.dismiss();
            if (isNotRemind) {
                presenter.notShowRemindAlert();
            }
        });
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }
}
