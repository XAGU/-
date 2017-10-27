package com.xiaolian.amigo.ui.wallet.adaptor;

import android.content.Context;

import com.xiaolian.amigo.R;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import lombok.Data;

/**
 * 充值提现详情
 * <p>
 * Created by zcd on 10/27/17.
 */

public class WithdrawRechargeDetailAdapter extends CommonAdapter<WithdrawRechargeDetailAdapter.Item>{

    public WithdrawRechargeDetailAdapter(Context context, int layoutId, List<Item> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, Item item, int position) {
        holder.setText(R.id.tv_title, item.getTitle());
        holder.setText(R.id.tv_content, item.getContent());
    }

    @Data
    public static class Item {
        private String title;
        private String content;

        public Item(String title, String content) {
            this.title = title;
            this.content = content;
        }
    }
}
