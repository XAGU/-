package com.xiaolian.amigo.ui.widget.indicator;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.xiaolian.amigo.ui.widget.ColorfulDotFlashView;
import com.xiaolian.amigo.util.DensityUtil;

/**
 * <p>
 * Created by zcd on 10/9/17.
 */

public class RefreshLayoutHeader extends LinearLayout implements RefreshHeader {

//    private ProgressDrawable mProgressDrawable;//刷新动画
    private ColorfulDotFlashView mProgressView;//刷新动画视图

    public RefreshLayoutHeader(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public RefreshLayoutHeader(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public RefreshLayoutHeader(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        setGravity(Gravity.CENTER);
//        mProgressDrawable = new ProgressDrawable();
        mProgressView = new ColorfulDotFlashView(context);
//        mProgressView.setImageDrawable(mProgressDrawable);
//        addView(mProgressView, DensityUtil.dp2px(20), DensityUtil.dp2px(20));
        addView(mProgressView);
        setMinimumHeight(DensityUtil.dp2px(60));

    }

    @Override
    public void onPullingDown(float percent, int offset, int headerHeight, int extendHeight) {

    }

    @Override
    public void onReleasing(float percent, int offset, int headerHeight, int extendHeight) {

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
    }

    @Override
    public int onFinish(RefreshLayout layout, boolean success) {
        return 0;
    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @Override
    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {
        switch (newState) {
            case PullDownToRefresh:
                mProgressView.startAnimation();//开始动画
                mProgressView.setVisibility(VISIBLE);
                break;
            case PullDownCanceled:
                mProgressView.setVisibility(GONE);
                mProgressView.endAnimation();
                break;
            case RefreshFinish:
                mProgressView.setVisibility(GONE);
                mProgressView.endAnimation();//停止动画
                break;

//            case None:
//            case ReleaseToRefresh:
//
//                mProgressView.setVisibility(GONE);//隐藏动画
//                break;
//            case Refreshing:
//                mProgressView.setVisibility(VISIBLE);//显示加载动画
//                break;
        }

    }
}
