package com.xiaolian.amigo.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class XLScrollView extends ScrollView {

    private OnScrollListener listener;

    /**
     * 设置滑动距离监听器
     */
    public void setOnScrollListener(OnScrollListener listener) {
        this.listener = listener;
    }

    public XLScrollView(Context context) {
        super(context);
    }

    public XLScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public XLScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // 滑动距离监听器
    public interface OnScrollListener{

        /**
         * 在滑动的时候调用，scrollY为已滑动的距离
         */
        void onScroll(int scrollY);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(listener!=null){
            listener.onScroll(getScrollY());
        }
    }
}
