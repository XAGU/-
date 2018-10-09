package com.xiaolian.amigo.ui.base;

import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.util.DensityUtil;
import com.xiaolian.amigo.util.Log;

import me.everything.android.ui.overscroll.IOverScrollDecor;
import me.everything.android.ui.overscroll.IOverScrollUpdateListener;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

/**
 * 统一添加toolbar
 *
 * @author zcd
 * @date 17/9/25
 */

public abstract class BaseToolBarActivity extends BaseActivity {

    private TextView tvToolbarTitle,tvTitle;
    private LinearLayout llMainContent;
    private ScrollView svMainContainer;
    private RelativeLayout rlToolBar;
    private View viewLine;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_toolbar);
        llMainContent = findViewById(R.id.ll_main_content);
        View layout = LayoutInflater.from(this).inflate(setLayout(), null, true);
        llMainContent.addView(layout);
        svMainContainer = findViewById(R.id.sv_main_container);
        IOverScrollDecor iOverScrollDecor = OverScrollDecoratorHelper.setUpOverScroll(svMainContainer);

        setUp();
        initToolBar();
        initInject();
        initView();

        iOverScrollDecor.setOverScrollUpdateListener((decor, state, offset) -> {
            if (offset < -(tvToolbarTitle.getHeight()) + tvToolbarTitle.getPaddingTop()) {
                setTitleVisiable(View.VISIBLE);
            } else {
                setTitleVisiable(View.GONE);
            }
        });
    }


    protected View getMainLayout() {
        return llMainContent;
    }

    protected ScrollView getScrollView() {
        return svMainContainer;
    }

    protected abstract void initInject();

    protected abstract void initView();

    protected void initToolBar() {
        tvToolbarTitle = (TextView) findViewById(R.id.tv_toolbar_title);
        tvTitle = findViewById(R.id.tv_title);
        rlToolBar = findViewById(R.id.rl_toolbar);
        viewLine = findViewById(R.id.view_line);
        setTitleVisiable(View.GONE);
        if (setTitle() != 0) {
            setToolBarTitle(setTitle());
        }
    }

    protected abstract @StringRes
    int setTitle();

    protected void setToolbarSubTitle(String subTitle, View.OnClickListener listener) {
        findViewById(R.id.tv_toolbar_sub_title).setVisibility(View.VISIBLE);
        ((TextView)findViewById(R.id.tv_toolbar_sub_title)).setText(subTitle);
        if (listener != null) {
            findViewById(R.id.tv_toolbar_sub_title).setOnClickListener(listener);
        }
    }

    protected void setMainBackground(@ColorRes int color) {
        svMainContainer.setBackgroundResource(color);
        rlToolBar.setBackgroundResource(color);
    }

    protected void setToolBarTitle(String title) {
        tvToolbarTitle.setText(title);
        tvTitle.setText(title);
    }

    protected void setToolBarTitle(@StringRes int res) {
        tvToolbarTitle.setText(res);
        tvTitle.setText(res);
    }

    private void setTitleVisiable(int visiable) {
        tvTitle.setVisibility(visiable);
        viewLine.setVisibility(visiable);
    }

    protected abstract @LayoutRes
    int setLayout();

    @Override
    protected void setUp() {

    }
}
