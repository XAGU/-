package com.xiaolian.amigo.activity.bonus.adaptor;

import android.content.Context;

import com.xiaolian.amigo.R;

/**
 * Created by adamzfc on 9/12/17.
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
