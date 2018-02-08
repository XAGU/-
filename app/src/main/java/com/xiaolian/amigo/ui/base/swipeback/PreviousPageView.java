package com.xiaolian.amigo.ui.base.swipeback;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

/**
 * @author zcd
 * @date 17/11/18
 */

class PreviousPageView extends View {

    private View mView;

    public PreviousPageView(Context context) {
        super(context);
    }

    public void cacheView(View view) {
        mView = view;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mView != null) {
            mView.draw(canvas);
            mView = null;
        }
    }
}

