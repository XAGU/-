package com.xiaolian.amigo.ui.widget.photoview.scrollerproxy;

import android.content.Context;

public class IcsScroller extends GingerScroller {

    public IcsScroller(Context context) {
        super(context);
    }

    @Override
    public boolean computeScrollOffset() {
        return mScroller.computeScrollOffset();
    }

}
