package com.xiaolian.amigo.ui.main.adaptor;

import android.graphics.Color;
import android.widget.RelativeLayout;

import com.xiaolian.amigo.R;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * <p>
 * Created by zcd on 10/11/17.
 */

public class HomeNormalDelegate implements ItemViewDelegate<HomeAdaptor.ItemWrapper> {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_home;
    }

    @Override
    public boolean isForViewType(HomeAdaptor.ItemWrapper item, int position) {
        return item.getType() == 1;
    }

    @Override
    public void convert(ViewHolder holder, HomeAdaptor.ItemWrapper itemWrapper, int position) {
        ((RelativeLayout)holder.getView(R.id.rl_item)).setBackgroundResource(itemWrapper.getRes());
        holder.setText(R.id.tv_device_name, itemWrapper.getDeviceName());
        holder.setText(R.id.tv_desc, itemWrapper.getDesc());
        holder.setTextColor(R.id.tv_desc, Color.parseColor(itemWrapper.getDescColor()));
    }
}
