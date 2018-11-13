package com.xiaolian.amigo.ui.device;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.base.BaseActivity;
import com.xiaolian.amigo.util.ScreenUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BleTipActivity extends BaseActivity {
    @BindView(R.id.ble_tip_title)
    TextView bleTipTitle;
    @BindView(R.id.ble_tip_content)
    TextView bleTipContent;
    @BindView(R.id.sv_main_container)
    ScrollView svMainContainer;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.view_line)
    View viewLine;
    @BindView(R.id.rl_toolbar)
    RelativeLayout rlToolbar;

    private int marginStart  ;

    private int marginEnd ;
    @Override
    protected void setUp() {

    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_ble_tip);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化view 操作
     */
    private void initView(){
        bleTipTitle.post(() -> {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) bleTipTitle.getLayoutParams();
            marginStart = params.getMarginStart();
            marginEnd = params.getMarginEnd() ;

            LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) bleTipContent.getLayoutParams();
            params1.setMargins(marginStart , ScreenUtils.dpToPxInt(BleTipActivity.this ,21) ,
                    marginEnd ,0);
            bleTipContent.setLayoutParams(params1);
        });
        getBlePermission();
    }


    @OnClick(R.id.iv_back)
    public void back(){
        setResult(RESULT_OK);
        this.finish();
    }


}
