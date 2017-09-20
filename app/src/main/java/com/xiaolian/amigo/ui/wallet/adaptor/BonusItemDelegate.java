package com.xiaolian.amigo.ui.wallet.adaptor;

import com.xiaolian.amigo.R;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * 红包
 * <p>
 * Created by zcd on 9/20/17.
 */

public class BonusItemDelegate implements ItemViewDelegate<RechargeAdaptor.RechargeWapper> {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_recharge;
    }

    @Override
    public boolean isForViewType(RechargeAdaptor.RechargeWapper item, int position) {
        return item.type == 2;
    }

    @Override
    public void convert(ViewHolder holder, RechargeAdaptor.RechargeWapper rechargeWapper, int position) {
        holder.setImageResource(R.id.iv_recharge, R.drawable.wallet_ticket);
        holder.setText(R.id.tv_recharge_main, rechargeWapper.main);
        holder.setText(R.id.tv_recharge_sub, rechargeWapper.sub);
    }
}
