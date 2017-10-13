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
 * <p>
 * Created by zcd on 9/25/17.
 */

public abstract class BaseToolBarActivity extends BaseActivity {

    private TextView tv_toolbar_title;
    private LinearLayout ll_main_content;
    private ScrollView sv_main_container;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_toolbar);
        ll_main_content = (LinearLayout) findViewById(R.id.ll_main_content);
        View layout = LayoutInflater.from(this).inflate(setLayout(), null, true);
        ll_main_content.addView(layout);
        sv_main_container = (ScrollView) findViewById(R.id.sv_main_container);
        OverScrollDecoratorHelper.setUpOverScroll(sv_main_container);
        setUp();
        initToolBar();
        initInject();
        initView();
    }

    protected View getMainLayout() {
        return ll_main_content;
    }

    protected abstract void initInject();

    protected abstract void initView();

    protected void initToolBar() {
        tv_toolbar_title = (TextView) findViewById(R.id.tv_toolbar_title);
        if (setTitle() != 0) {
            setToolBarTitle(setTitle());
        }
    }

    protected abstract @StringRes int setTitle();

    protected void setMainBackground(@ColorRes int color) {
        sv_main_container.setBackgroundResource(color);
    }

    protected void setToolBarTitle(String title) {
        tv_toolbar_title.setText(title);
    }

    protected void setToolBarTitle(@StringRes int res) {
        tv_toolbar_title.setText(res);
    }

    protected abstract @LayoutRes int setLayout();

    @Override
    protected void setUp() {

    }
}
