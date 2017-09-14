package com.xiaolian.amigo.activity.lostandfound;

import android.os.Bundle;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.base.BaseActivity;

import butterknife.ButterKnife;

/**
 * 招领发布
 * @author zcd
 */

public class PublishFoundActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_found);
        ButterKnife.bind(this);
    }
}
