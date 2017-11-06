package com.xiaolian.amigo.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.base.BaseActivity;
import com.xiaolian.amigo.ui.widget.swipebutton.SlideUnlockView;

/**
 * <p>
 * Created by zcd on 17/11/6.
 */

public class TestActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        SlideUnlockView slideUnlockView = (SlideUnlockView) findViewById(R.id.slideView);
        slideUnlockView.setOnUnLockListener(new SlideUnlockView.OnUnLockListener() {
            @Override
            public void setUnLocked(boolean lock) {
            }
        });
    }

    @Override
    protected void setUp() {

    }
}
