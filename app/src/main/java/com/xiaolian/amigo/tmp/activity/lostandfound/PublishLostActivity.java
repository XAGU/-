package com.xiaolian.amigo.tmp.activity.lostandfound;

import android.os.Bundle;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.tmp.base.BaseActivity;

import butterknife.ButterKnife;

/**
 * 失物发布
 * @author zcd
 */

public class PublishLostActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_lost);
        ButterKnife.bind(this);
    }
}
