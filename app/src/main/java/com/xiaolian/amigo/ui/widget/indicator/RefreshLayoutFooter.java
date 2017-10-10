package com.xiaolian.amigo.ui.widget.indicator;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.xiaolian.amigo.util.DensityUtil;

/**
 * <p>
 * Created by zcd on 10/9/17.
 */

public class RefreshLayoutFooter extends LinearLayout implements RefreshFooter {

    private ProgressDrawable mProgressDrawable;//刷新动画
    private ImageView mProgressView;//刷新动画视图

    public RefreshLayoutFooter(Context context) {
        super(context);
        initView(context);
    }


    public RefreshLayoutFooter(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public RefreshLayoutFooter(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }
    private void initView(Context context) {
        setGravity(Gravity.CENTER);
        mProgressDrawable = new ProgressDrawable();
        mProgressView = new ImageView(context);
        mProgressView.setImageDrawable(mProgressDrawable);
        addView(mProgressView, DensityUtil.dp2px(20), DensityUtil.dp2px(20));
        setMinimumHeight(DensityUtil.dp2px(60));
    }

    @Override
    public void onPullingUp(float percent, int offset, int footerHeight, int extendHeight) {

    }

    @Override
    public void onPullReleasing(float percent, int offset, int footerHeight, int extendHeight) {

    }

    @Override
    public boolean setLoadmoreFinished(boolean finished) {
        return false;
    }

    @NonNull
    @Override
    public View getView() {
        return this;
    }

    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;//指定为平移，不能null
    }

    @Override
    public void setPrimaryColors(@ColorInt int... colors) {

    }

    @Override
    public void onInitialized(RefreshKernel kernel, int height, int extendHeight) {

    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public void onStartAnimator(RefreshLayout layout, int height, int extendHeight) {
        mProgressView.setVisibility(VISIBLE);
        mProgressDrawable.start();//开始动画
    }

    @Override
    public int onFinish(RefreshLayout layout, boolean success) {
        mProgressDrawable.stop();//停止动画
        mProgressView.setVisibility(GONE);
        return 0;//延迟500毫秒之后再弹回
    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @Override
    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {
//        switch (newState) {
//            case None:
//                mProgressView.setVisibility(GONE);//隐藏动画
//                break;
//            case PullToUpLoad:
//                mProgressView.setVisibility(VISIBLE);//显示加载动画
//                break;
//        }
    }
}
