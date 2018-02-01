package com.xiaolian.amigo.ui.wallet.adaptor;

import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * 标题＋内容＋复制
 *
 * @author zcd
 * @date 17/11/13
 */

public class TitleContentCopyDelegate implements ItemViewDelegate<WithdrawRechargeDetailAdapter.Item> {
    public interface OnCopyListener {
        /**
         * 复制
         * @param string 复制内容
         */
        void onCopy(String string);
    }

    private OnCopyListener listener;

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_title_content_copy;
    }

    @Override
    public boolean isForViewType(WithdrawRechargeDetailAdapter.Item item, int position) {
        return item.getType() == 2;
    }

    @Override
    public void convert(ViewHolder holder, WithdrawRechargeDetailAdapter.Item item, int position) {
        holder.setText(R.id.tv_title, item.getTitle());
        holder.setText(R.id.tv_content, item.getContent());
        holder.getView(R.id.tv_copy).setOnClickListener((v) -> {
            if (listener != null) {
                listener.onCopy(((TextView) holder.getView(R.id.tv_content)).getText().toString());
            }
        });
    }

    public void setCopyListener(OnCopyListener listener) {
        this.listener = listener;
    }
}
