package com.xiaolian.amigo.ui.widget.recyclerview;

import android.content.Context;

import com.xiaolian.amigo.R;

/**
 * DefaultLoadMoreFooterView
 */

public class DefaultLoadMoreFooterView extends BaseLoadMoreFooterView {
    public DefaultLoadMoreFooterView(Context context) {
        super(context);
    }

    @Override
    public int getLoadMoreLayoutResource() {
        return R.layout.view_default_load_more;
    }
}
