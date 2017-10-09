package com.xiaolian.amigo.ui.main;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.device.heater.HeaterActivity;
import com.xiaolian.amigo.ui.device.dispenser.DispenserActivity;
import com.xiaolian.amigo.ui.lostandfound.LostAndFoundActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.xiaolian.amigo.data.enumeration.Device.DISPENSER;
import static com.xiaolian.amigo.data.enumeration.Device.HEARTER;

/**
 * Created by yik on 2017/9/5.
 */

public class HomeFragment extends Fragment {

    private static final String TAG = HomeFragment.class.getSimpleName();

    /**
     * 热水器
     */
    @BindView(R.id.rl_geyser)
    RelativeLayout rl_geyser;

    /**
     * 点击进入热水器界面
     */
    @OnClick(R.id.rl_geyser)
    public void gotoGeyser() {
        MainActivity parent  = (MainActivity)this.getActivity();
        parent.setBleCallback(() -> parent.checkTimeValid(HEARTER, HeaterActivity.class));
        parent.getBlePermission();
    }


    /**
     * 饮水机
     */
    @BindView(R.id.rl_water_fountain)
    RelativeLayout rl_water_fountain;

    /**
     * 点击进入饮水机页面
     */
    @OnClick(R.id.rl_water_fountain)
    public void gotoWaterFountain() {
        MainActivity parent  = (MainActivity)this.getActivity();
        parent.setBleCallback(() -> parent.checkTimeValid(DISPENSER, DispenserActivity.class));
        parent.getBlePermission();
    }

    @OnClick(R.id.rl_lost_and_found)
    public void gotoLostAndFound() {
        ((MainActivity) getActivity()).startActivity(LostAndFoundActivity.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View homeView = inflater.inflate(R.layout.activity_tab_home, container, false);
        ButterKnife.bind(this, homeView);
        return homeView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
