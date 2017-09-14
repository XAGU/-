package com.xiaolian.amigo.tmp.activity;


import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.transition.Transition;
import android.view.View;
import android.widget.ImageView;

import com.xiaolian.amigo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yik on 2017/9/5.
 */

public class MainActivity extends Activity {

    @BindView(R.id.bt_switch)
    ImageView btSwitch;

    HomeFragment homeFragment;
    ProfileFragment profileFragment;

    int current = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        btSwitch.setBackgroundResource(R.drawable.profile);

        homeFragment = new HomeFragment();
        FragmentManager fragmentManager = this.getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fm_container, homeFragment);
        transaction.commit();
    }

    @OnClick(R.id.bt_switch)
    void onSwitch(View v) {
        ImageView imageView = (ImageView) v;
        FragmentManager fragmentManager = this.getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (current == 0) {
            transaction.remove(homeFragment);
            if (profileFragment == null) {
                profileFragment = new ProfileFragment();
            }
            transaction.add(R.id.fm_container, profileFragment);
            transaction.commit();

            imageView.setBackgroundResource(R.drawable.home);
            current = 1;
        } else {
            transaction.remove(profileFragment);
            transaction.add(R.id.fm_container, homeFragment);
            transaction.commit();
            imageView.setBackgroundResource(R.drawable.profile);
            current = 0;
        }


    }
}
