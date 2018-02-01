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
 * 选择报修
 *
 * @author zcd
 * @date 17/11/10
 */

public class ChooseRepairActivity extends RepairBaseActivity {
    @BindView(R.id.tv_heater)
    TextView tvHeater;
    @BindView(R.id.tv_dispenser)
    TextView tvDispenser;
    @BindView(R.id.tv_dryer)
    TextView tvDryer;
    @BindView(R.id.tv_washer)
    TextView tvWasher;
    @BindView(R.id.tv_default_dormitory)
    TextView tvDefaultDormitory;
    @BindView(R.id.tv_favorite)
    TextView tvFavorite;

    @BindView(R.id.iv_heater_tick)
    ImageView ivHeaterTick;
    @BindView(R.id.iv_dispenser_tick)
    ImageView ivDispenserTick;
    @BindView(R.id.iv_dryer_tick)
    ImageView ivDryerTick;
    @BindView(R.id.iv_washer_tick)
    ImageView ivWasherTick;
    @BindView(R.id.iv_default_tick)
    ImageView ivDefaultTick;
    @BindView(R.id.iv_favorite_tick)
    ImageView ivFavoriteTick;

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
        ivHeaterTick.setVisibility(View.VISIBLE);
        Intent intent = new Intent(this, ListChooseActivity.class);
        intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_CHOOSE_ACTION, ListChooseActivity.ACTION_LIST_BUILDING);
        intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_DEVICE_TYPE, Device.HEATER.getType());
        intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_SRC_ACTIVITY, Constant.REPAIR_APPLY_ACTIVITY_SRC);
        startActivity(intent);
    }

    @OnClick(R.id.rl_dispenser)
    void onDispenserClick() {
        clearTick();
        ivDispenserTick.setVisibility(View.VISIBLE);
        Intent intent = new Intent(this, ListChooseActivity.class);
        intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_CHOOSE_ACTION, ListChooseActivity.ACTION_LIST_BUILDING);
        intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_DEVICE_TYPE, Device.DISPENSER.getType());
        intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_SRC_ACTIVITY, Constant.REPAIR_APPLY_ACTIVITY_SRC);
        startActivity(intent);
    }

    @OnClick(R.id.rl_dryer)
    void onDryerClick() {
        clearTick();
        ivDryerTick.setVisibility(View.VISIBLE);
        Intent intent = new Intent(this, ListChooseActivity.class);
        intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_CHOOSE_ACTION, ListChooseActivity.ACTION_LIST_BUILDING);
        intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_DEVICE_TYPE, Device.DRYER.getType());
        intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_SRC_ACTIVITY, Constant.REPAIR_APPLY_ACTIVITY_SRC);
        startActivity(intent);
    }

    @OnClick(R.id.rl_washer)
    void onWasherClick() {
        clearTick();
        ivWasherTick.setVisibility(View.VISIBLE);
        Intent intent = new Intent(this, ListChooseActivity.class);
        intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_CHOOSE_ACTION, ListChooseActivity.ACTION_LIST_BUILDING);
        intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_DEVICE_TYPE, Device.WASHER.getType());
        intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_SRC_ACTIVITY, Constant.REPAIR_APPLY_ACTIVITY_SRC);
        startActivity(intent);
    }

    @OnClick(R.id.rl_default_dormitory)
    void onDefaultDormitoryClick() {
        clearTick();
        ivDefaultTick.setVisibility(View.VISIBLE);
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
        ivFavoriteTick.setVisibility(View.VISIBLE);
        Intent intent = new Intent(this, FavoriteActivity.class);
        intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_CHOOSE_ACTION, ListChooseActivity.ACTION_LIST_BUILDING);
        intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_DEVICE_TYPE, Device.DISPENSER.getType());
        intent.putExtra(ListChooseActivity.INTENT_KEY_LIST_SRC_ACTIVITY, Constant.REPAIR_APPLY_ACTIVITY_SRC);
        intent.putExtra(Constant.INTENT_ACTION, IntentAction.ACTION_CHOOSE_FAVORITE_FOR_REPAIR.getType());
        startActivity(intent);
    }

    private void clearTick() {
        ivDefaultTick.setVisibility(View.GONE);
        ivDispenserTick.setVisibility(View.GONE);
        ivDryerTick.setVisibility(View.GONE);
        ivWasherTick.setVisibility(View.GONE);
        ivHeaterTick.setVisibility(View.GONE);
        ivFavoriteTick.setVisibility(View.GONE);
    }
}
