package com.xiaolian.amigo.ui.base;

import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.xiaolian.amigo.R;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

/**
 * 统一添加toolbar
 *
 * @author zcd
 * @date 17/9/25
 */

public abstract class BaseToolBarActivity extends BaseActivity {

    private TextView tvToolbarTitle;
    private LinearLayout llMainContent;
    private ScrollView svMainContainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_toolbar);
        llMainContent = findViewById(R.id.ll_main_content);
        View layout = LayoutInflater.from(this).inflate(setLayout(), null, true);
        llMainContent.addView(layout);
        svMainContainer = findViewById(R.id.sv_main_container);
        OverScrollDecoratorHelper.setUpOverScroll(svMainContainer);
        setUp();
        initToolBar();
        initInject();
        initView();
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
        if (setTitle() != 0) {
            setToolBarTitle(setTitle());
        }
    }

    protected abstract @StringRes
    int setTitle();

    protected void setMainBackground(@ColorRes int color) {
        svMainContainer.setBackgroundResource(color);
    }

    protected void setToolBarTitle(String title) {
        tvToolbarTitle.setText(title);
    }

    protected void setToolBarTitle(@StringRes int res) {
        tvToolbarTitle.setText(res);
    }

    protected abstract @LayoutRes
    int setLayout();

    @Override
    protected void setUp() {

    }
}
