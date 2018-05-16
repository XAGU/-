package com.xiaolian.amigo.ui.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author caidong
 * @date 17/9/12
 */

public class SpaceBottomItemDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public SpaceBottomItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildLayoutPosition(view) != 0)
            outRect.bottom = space;
    }

}
