package com.xiaolian.amigo.ui.repair;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.Device;
import com.xiaolian.amigo.data.enumeration.IntentAction;
import com.xiaolian.amigo.data.prefs.ISharedPreferencesHelp;
import com.xiaolian.amigo.ui.favorite.FavoriteActivity;
import com.xiaolian.amigo.ui.user.ListChooseActivity;
import com.xiaolian.amigo.util.Constant;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <p>
 * Created by zcd on 17/11/10.
 */

public class ChooseRepairActivity extends RepairBaseActivity {
    @BindView(R.id.tv_heater)
    TextView tv_heater;
    @BindView(R.id.tv_dispenser)
    TextView tv_dispenser;
    @BindView(R.id.tv_dryer)
    TextView tv_dryer;
    @BindView(R.id.tv_default_dormitory)
    TextView tv_default_dormitory;
    @BindView(R.id.tv_favorite)
    TextView tv_favorite;

    @BindView(R.id.iv_heater_tick)
    ImageView iv_heater_tick;
    @BindView(R.id.iv_dispenser_tick)
    ImageView iv_dispenser_tick;
    @BindView(R.id.iv_dryer_tick)
    ImageView iv_dryer_tick;
    @BindView(R.id.iv_default_tick)
    ImageView iv_default_tick;
    @BindView(R.id.iv_favorite_tick)
    ImageView iv_favorite_tick;

    @Inject
    ISharedPreferencesHelp sharedPreferencesHelp;

    @Override
    protected void initView() {
        setMainBackground(R.color.white);
        setUnBinder(ButterKnife.bind(this));
        getActivityComponent().inject(this);
        clearTick();
    }

    @Override
    protected int setTitle() {
        return R.string.device_type;
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_choose_repair;
    }

    @OnClick(R.id.rl_heater)
    void onHeaterClick() {
        clearTick();
        iv_heater_tick.setVisibility(View.VISIBLE);
        Intent intent = new Intent(this, ListChooseActivity.class);
        intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_CHOOSE_ACTION, ListChooseActivity.ACTION_LIST_BUILDING);
        intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_DEVICE_TYPE, Device.HEATER.getType());
        intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_SRC_ACTIVITY, Constant.REPAIR_APPLY_ACTIVITY_SRC);
        startActivity(intent);
    }

    @OnClick(R.id.rl_dispenser)
    void onDispenserClick() {
        clearTick();
        iv_dispenser_tick.setVisibility(View.VISIBLE);
        Intent intent = new Intent(this, ListChooseActivity.class);
        intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_CHOOSE_ACTION, ListChooseActivity.ACTION_LIST_BUILDING);
        intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_DEVICE_TYPE, Device.DISPENSER.getType());
        intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_SRC_ACTIVITY, Constant.REPAIR_APPLY_ACTIVITY_SRC);
        startActivity(intent);
    }

    @OnClick(R.id.rl_dryer)
    void onDryerClick() {
        clearTick();
        iv_dryer_tick.setVisibility(View.VISIBLE);
        Intent intent = new Intent(this, ListChooseActivity.class);
        intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_CHOOSE_ACTION, ListChooseActivity.ACTION_LIST_BUILDING);
        intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_DEVICE_TYPE, Device.DRYER.getType());
        intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_SRC_ACTIVITY, Constant.REPAIR_APPLY_ACTIVITY_SRC);
        startActivity(intent);
    }

    @OnClick(R.id.rl_default_dormitory)
    void onDefaultDormitoryClick() {
        clearTick();
        iv_default_tick.setVisibility(View.VISIBLE);
        if (TextUtils.isEmpty(sharedPreferencesHelp.getUserInfo().getResidenceName())) {
            onError(getString(R.string.dormitory_empty_tip));
            return;
        }
        if (TextUtils.isEmpty(sharedPreferencesHelp.getUserInfo().getMacAddress())) {
            onError(getString(R.string.mac_address_empty_tip));
            return;
        }
        Intent intent = new Intent(this, RepairApplyActivity.class);
        intent.putExtra(Constant.LOCATION_ID, sharedPreferencesHelp.getUserInfo().getResidenceId());
        intent.putExtra(Constant.DEVICE_TYPE, Device.HEATER.getType());
        intent.putExtra(Constant.LOCATION, Device.HEATER.getDesc()
                + Constant.CHINEASE_COLON + sharedPreferencesHelp.getUserInfo().getResidenceName());
        startActivity(intent);
    }

    @OnClick(R.id.rl_favorite)
    void onFavoriteClick() {
        clearTick();
        iv_favorite_tick.setVisibility(View.VISIBLE);
        Intent intent = new Intent(this, FavoriteActivity.class);
        intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_CHOOSE_ACTION, ListChooseActivity.ACTION_LIST_BUILDING);
        intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_DEVICE_TYPE, Device.DISPENSER.getType());
        intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_SRC_ACTIVITY, Constant.REPAIR_APPLY_ACTIVITY_SRC);
        intent.putExtra(Constant.INTENT_ACTION, IntentAction.ACTION_CHOOSE_FAVORITE_FOR_REPAIR.getType());
        startActivity(intent);
    }

    private void clearTick() {
        iv_default_tick.setVisibility(View.GONE);
        iv_dispenser_tick.setVisibility(View.GONE);
        iv_dryer_tick.setVisibility(View.GONE);
        iv_heater_tick.setVisibility(View.GONE);
        iv_favorite_tick.setVisibility(View.GONE);
    }
}
