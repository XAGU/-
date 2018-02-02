package com.xiaolian.amigo.ui.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author zcd
 * @date 17/11/6
 */

public class CustomViewPager extends ViewPager {

    public int canNotScrollPageIndex = -1;

    private boolean scrollble = true;

    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        if (!scrollble) {
            return true;
        }
        return getCurrentItem() == getCanNotScrollPageIndex();
    }

    public int getCanNotScrollPageIndex() {
        return canNotScrollPageIndex;
    }

    public void setCanNotScrollPageIndex(int canNotScrollPageIndex) {
        this.canNotScrollPageIndex = canNotScrollPageIndex;
    }

    public boolean isScrollble() {
        return scrollble;
    }

    public void setScrollble(boolean scrollble) {
        this.scrollble = scrollble;
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        if (!scrollble) {
            return false;
        }
        return super.onTouchEvent(arg0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (!scrollble) {
            return false;
        }
        return super.onInterceptTouchEvent(arg0);
    }
}
