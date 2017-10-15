package com.xiaolian.amigo.ui.wallet.adaptor;

import com.xiaolian.amigo.R;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * 打折
 * <p>
 * Created by zcd on 9/20/17.
 */

public class DiscountItemDelegate implements ItemViewDelegate<RechargeAdaptor.RechargeWrapper> {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_recharge;
    }

    @Override
    public boolean isForViewType(RechargeAdaptor.RechargeWrapper item, int position) {
        return item.type == 1;
    }

    @Override
    public void convert(ViewHolder holder, RechargeAdaptor.RechargeWrapper rechargeWrapper, int position) {
        holder.setImageResource(R.id.iv_recharge, R.drawable.wallet_discount);
        holder.setText(R.id.tv_recharge_main, rechargeWrapper.main);
        holder.setText(R.id.tv_recharge_sub, rechargeWrapper.sub);
    }
}
