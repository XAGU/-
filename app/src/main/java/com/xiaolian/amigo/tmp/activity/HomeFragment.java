package com.xiaolian.amigo.tmp.activity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.tmp.activity.device.geyser.GeyserActivity;
import com.xiaolian.amigo.tmp.activity.device.waterfountain.WaterFountainActivity;
import com.xiaolian.amigo.tmp.activity.lostandfound.LostAndFoundActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yik on 2017/9/5.
 */

public class HomeFragment extends Fragment {

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
        startActivity(new Intent(getActivity(), GeyserActivity.class));
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
        startActivity(new Intent(getActivity(), WaterFountainActivity.class));
    }

    @OnClick(R.id.rl_lost_and_found)
    public void gotoLostAndFound() {
        startActivity(new Intent(getActivity(), LostAndFoundActivity.class));
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
