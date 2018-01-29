package com.xiaolian.amigo.ui.wallet.adaptor;

import com.xiaolian.amigo.R;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * 标题＋内容
 * <p>
 * Created by zcd on 17/11/13.
 */

public class TitleContentDelegate implements ItemViewDelegate<WithdrawRechargeDetailAdapter.Item> {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_title_content;
    }

    @Override
    public boolean isForViewType(WithdrawRechargeDetailAdapter.Item item, int position) {
        return item.getType() == WithdrawRechargeDetailAdapter.TITLE_CONTENT_TYPE;
    }

    @Override
    public void convert(ViewHolder holder, WithdrawRechargeDetailAdapter.Item item, int position) {
        holder.setText(R.id.tv_title, item.getTitle());
        holder.setText(R.id.tv_content, item.getContent());
    }
}
