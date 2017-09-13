package com.xiaolian.amigo.activity.device.waterfountain;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 饮水机页面
 * @author zcd
 */

public class WaterFountainActivity extends BaseActivity {

    @BindView(R.id.ll_header)
    LinearLayout ll_header;

    /**
     * 设备名称
     */
    @BindView(R.id.tv_device_name)
    TextView tv_device_name;

    /**
     * 收藏
     */
    @BindView(R.id.iv_collect)
    ImageView iv_collect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_water_fountain);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT < 19) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, 0, 0);
            ll_header.setLayoutParams(layoutParams);
            LinearLayout contentView = (LinearLayout) findViewById(R.id.ll_content);
            LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            contentView.setLayoutParams(layoutParams1);
        }
        initView();
    }

    private void initView() {
        tv_device_name.setText("XX楼X层具体位置");
        iv_collect.setVisibility(View.VISIBLE);
    }

}
