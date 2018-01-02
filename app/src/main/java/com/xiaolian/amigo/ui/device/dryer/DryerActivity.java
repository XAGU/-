package com.xiaolian.amigo.ui.device.dryer;

import android.view.View;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.device.WaterDeviceBaseActivity;
import com.xiaolian.amigo.ui.device.intf.dryer.IDryerPresenter;
import com.xiaolian.amigo.ui.device.intf.dryer.IDryerView;

import javax.inject.Inject;


/**
 * <p>
 * Created by zcd on 18/1/2.
 */

public class DryerActivity extends WaterDeviceBaseActivity<IDryerPresenter> implements IDryerView {
    public static final String INTENT_KEY_FAVOR = "intent_key_favor";
    public static final String INTENT_KEY_ID = "intent_key_id";
    private boolean isFavor;
    private Long id;

    @Inject
    IDryerPresenter<IDryerView> presenter;

    @Override
    protected void setUp() {
        super.setUp();
        if (getIntent() != null) {
            isFavor = getIntent().getBooleanExtra(INTENT_KEY_FAVOR, false);
            id = getIntent().getLongExtra(INTENT_KEY_ID, -1);
        }
    }

    @Override
    public void showGuide() {

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
            // TODO 设置饮水机事件
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
    public IDryerPresenter setPresenter() {
        return presenter;
    }

    @Override
    public void onBackPressed() {
        back2Main();
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }
}
