package com.xiaolian.amigo.ui.wallet.adaptor;

import com.xiaolian.amigo.R;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * 普通充值
 * <p>
 * Created by zcd on 9/20/17.
 */

public class NormalItemDelegate implements ItemViewDelegate<RechargeAdaptor.RechargeWapper> {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_recharge_normal;
    }

    @Override
    public boolean isForViewType(RechargeAdaptor.RechargeWapper item, int position) {
        return item.type != 1 && item.type != 2;
    }

    @Override
    public void convert(ViewHolder holder, RechargeAdaptor.RechargeWapper rechargeWapper, int position) {
        holder.setText(R.id.tv_recharge_main, rechargeWapper.main);
    }
}
