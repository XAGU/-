package com.xiaolian.amigo.tmp.activity;

import android.app.ActivityGroup;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ScrollView;
import android.widget.TextView;

import com.xiaolian.amigo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by caidong on 2017/9/2.
 */

public class LoginRegistryGroupActivity extends ActivityGroup {

    @BindView(R.id.tv_login)
    TextView tv_login;
    @BindView(R.id.tv_registry)
    TextView tv_registry;
    @BindView(R.id.sv_container)
    ScrollView sc_container;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_registry_group);
        ButterKnife.bind(this);

        sc_container.removeAllViews();
        sc_container.addView(getLocalActivityManager().startActivity(
                "Login", new Intent(this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView());

        tv_login.setClickable(false);
    }

    @OnClick(R.id.tv_registry)
    void registry() {
        sc_container.removeAllViews();
        sc_container.addView(getLocalActivityManager().startActivity(
                "Registry", new Intent(this, RegistryActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView());

        tv_login.setClickable(true);
        tv_registry.setClickable(false);

        tv_login.setTextColor(Color.parseColor("#bbbbbb"));
        tv_registry.setTextColor(Color.parseColor("#222222"));
    }

    @OnClick(R.id.tv_login)
    void login() {
        sc_container.removeAllViews();
        sc_container.addView(getLocalActivityManager().startActivity(
                "Login", new Intent(this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)).getDecorView());

        tv_login.setClickable(false);
        tv_registry.setClickable(true);

        tv_login.setTextColor(Color.parseColor("#222222"));
        tv_registry.setTextColor(Color.parseColor("#bbbbbb"));
    }

}
