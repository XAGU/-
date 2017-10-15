package com.xiaolian.amigo.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.xiaolian.amigo.R;

/**
 * <p>
 * Created by zcd on 10/15/17.
 */

public class SplashActivity extends MainBaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        startActivity(new Intent(this, MainActivity.class));
    }
}
