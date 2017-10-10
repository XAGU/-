package com.xiaolian.amigo.ui.widget.pageloader;

import android.content.Context;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.widget.recyclerview.BaseLoadMoreFooterView;

/**
 * DefaultLoadMoreFooterView LoadMoreView默认实现
 * @author zcd
 */

public class DefaultLoadMoreView extends BaseLoadMoreFooterView {
    public DefaultLoadMoreView(Context context) {
        super(context);
    }

    @Override
    public int getLoadMoreLayoutResource() {
        return R.layout.view_default_load_more;
    }
}
