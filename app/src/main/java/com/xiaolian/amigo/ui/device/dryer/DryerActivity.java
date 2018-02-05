package com.xiaolian.amigo.ui.device.dryer;

import android.view.View;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.device.WaterDeviceBaseActivity;
import com.xiaolian.amigo.ui.device.dispenser.DispenserActivity;
import com.xiaolian.amigo.ui.device.intf.dryer.IDryerPresenter;
import com.xiaolian.amigo.ui.device.intf.dryer.IDryerView;

import javax.inject.Inject;


/**
 * 吹风机页面
 *
 * @author zcd
 * @date 18/1/2
 */

public class DryerActivity extends WaterDeviceBaseActivity<IDryerPresenter> implements IDryerView {
    private boolean isFavor;
    private Long id;

    @Inject
    IDryerPresenter<IDryerView> presenter;

    @Override
    protected void setUp() {
        super.setUp();
        if (getIntent() != null) {
            isFavor = getIntent().getBooleanExtra(DispenserActivity.INTENT_KEY_FAVOR, false);
            id = getIntent().getLongExtra(DispenserActivity.INTENT_KEY_ID, -1);
        }
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
    protected void initInject() {
        getActivityComponent().inject(this);
        presenter.onAttach(this);
    }

    @Override
    protected int setHeaderBackgroundDrawable() {
        return R.drawable.bg_rect_yellow;
    }

    @Override
    protected int setBottomBackgroundColor() {
        return R.color.dryer_gradient_start;
    }

    @Override
    protected String setSubtitleString() {
        return getString(R.string.change_hair_dryer);
    }

    @Override
    protected View.OnClickListener setTitleClickListener() {
        return v -> changeDryer();
    }

    @Override
    protected View.OnClickListener setTopRightIconClickListener() {
        return v -> {
            if (isFavor) {
                presenter.unFavorite(id);
            } else {
                presenter.favorite(id);
            }
        };
    }

    @Override
    public void setFavoriteIcon() {
        isFavor = true;
        setTopRightIcon(R.drawable.collected);
    }

    @Override
    public void setUnFavoriteIcon() {
        isFavor = false;
        setTopRightIcon(R.drawable.uncollected);
    }

    @Override
    protected int setTopRightIconDrawable() {
        if (isFavor) {
            return R.drawable.collected;
        } else {
            return R.drawable.uncollected;
        }
    }

    @Override
    protected int setHeaderIconDrawable() {
        return R.drawable.ic_hair_dryer;
    }

    @Override
    protected String getSlideButtonText() {
        return getString(R.string.comma_start_dryer);
    }

    @Override
    protected String getBalanceTradeTip() {
        return getString(R.string.balance_trade_tip_dryer);
    }

    @Override
    public IDryerPresenter setPresenter() {
        return presenter;
    }

    @Override
    public void onBackPressed() {
        back2Main();
    }
}
