package com.xiaolian.amigo.ui.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;

import static java.lang.System.currentTimeMillis;

public class HideSmartLayout extends SmartRefreshLayout {
    public HideSmartLayout(Context context) {
        super(context);
    }

    public HideSmartLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HideSmartLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @SuppressLint("RestrictedApi")
    @Override
    protected void setStateRefreshing() {

        AnimatorListenerAdapter listener = new AnimatorListenerAdapter() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onAnimationEnd(Animator animation) {
                mLastRefreshingTime = currentTimeMillis();
                notifyStateChanged(RefreshState.Refreshing);
                if (mRefreshListener != null) {
                    mRefreshListener.onRefresh(HideSmartLayout.this);
                }
                if (mRefreshHeader != null) {
                    mRefreshHeader.onStartAnimator(HideSmartLayout.this, mHeaderHeight, mHeaderExtendHeight);
                }
                if (mOnMultiPurposeListener != null) {
                    mOnMultiPurposeListener.onRefresh(HideSmartLayout.this);
                    mOnMultiPurposeListener.onHeaderStartAnimator(mRefreshHeader, mHeaderHeight, mHeaderExtendHeight);
                }
            }

            @SuppressLint("RestrictedApi")
            @Override
            public void onAnimationStart(Animator animation, boolean isReverse) {
                super.onAnimationStart(animation , isReverse);
                if(mRefreshHeader != null)
                mRefreshHeader.getView().setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                if(mRefreshHeader != null)
                    mRefreshHeader.getView().setVisibility(VISIBLE);
            }
        };
        notifyStateChanged(RefreshState.RefreshReleased);
        ValueAnimator animator = animSpinner(mHeaderHeight);
        if (animator != null) {
            animator.addListener(listener);
        }
        if (mRefreshHeader != null) {
            //onReleased 的执行顺序定在 animSpinner 之后 onAnimationEnd 之前
            // 这样 onRefreshReleased内部 可以做出 对 前面 animSpinner 的覆盖 操作
            mRefreshHeader.getView().setVisibility(VISIBLE);
            mRefreshHeader.onReleased(this, mHeaderHeight, mHeaderExtendHeight);
        }
        if (mOnMultiPurposeListener != null) {
            //同 mRefreshHeader.onReleased 一致
            mOnMultiPurposeListener.onHeaderReleased(mRefreshHeader, mHeaderHeight, mHeaderExtendHeight);
        }
        if (animator == null) {
            //onAnimationEnd 会改变状态为 Refreshing 必须在 onReleased 之后调用
            listener.onAnimationEnd(null);
        }


    }


    @SuppressLint("RestrictedApi")
    @Override
    protected void onAnimationStart() {
        super.onAnimationStart();
        if (mRefreshHeader != null)
        mRefreshHeader.getView().setVisibility(VISIBLE);
    }
}
