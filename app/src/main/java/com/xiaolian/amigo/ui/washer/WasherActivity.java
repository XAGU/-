package com.xiaolian.amigo.ui.washer;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.base.BaseActivity;
import com.xiaolian.amigo.ui.base.BaseToolBarActivity;
import com.xiaolian.amigo.ui.washer.intf.IWashView;

/**
 * 洗衣机首页
 * <p>
 * Created by zcd on 18/1/12.
 */

public class WasherActivity extends WasherBaseActivity implements IWashView {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_washer);
    }

    @Override
    protected void setUp() {

    }

}
