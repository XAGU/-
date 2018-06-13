package com.xiaolian.amigo.ui.wallet.adaptor;

import android.text.TextUtils;
import android.util.TypedValue;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * 标题＋内容
 *
 * @author zcd
 * @date 17/11/13
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
        if (TextUtils.equals(item.getTitle(), "设备信息：")) {
            ((TextView)holder.getView(R.id.tv_content)).setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        } else {
            ((TextView)holder.getView(R.id.tv_content)).setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        }
        holder.setText(R.id.tv_title, item.getTitle());
        holder.setText(R.id.tv_content, item.getContent());
    }
}
