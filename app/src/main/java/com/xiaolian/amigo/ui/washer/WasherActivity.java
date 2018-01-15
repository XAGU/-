package com.xiaolian.amigo.ui.washer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.LinearLayout;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.washer.intf.IWasherPresenter;
import com.xiaolian.amigo.ui.washer.intf.IWasherView;

import javax.inject.Inject;

/**
 * 洗衣机首页
 * <p>
 * Created by zcd on 18/1/12.
 */

public class WasherActivity extends WasherBaseActivity implements IWasherView {
    @Inject
    IWasherPresenter<IWasherView> presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_washer);
        bindView();
    }

    private void bindView() {
        LinearLayout ll_start_wash = findViewById(R.id.ll_start_wash);
        ll_start_wash.setOnClickListener(v ->
            startActivity(new Intent(WasherActivity.this, ChooseWashModeActivity.class)));
        findViewById(R.id.iv_back).setOnClickListener(v -> {
            hideLoading();
            onBackPressed();
        });
    }

    @Override
    protected void setUp() {

    }

}
