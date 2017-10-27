package com.xiaolian.amigo.ui.wallet.adaptor;

import android.content.Context;

import com.xiaolian.amigo.R;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import lombok.Data;

/**
 * 充值fAdaptor
 * <p>
 * Created by zcd on 9/20/17.
 */

public class RechargeTypeAdaptor extends CommonAdapter<RechargeTypeAdaptor.RechargeWrapper> {
    public RechargeTypeAdaptor(Context context, int layoutId, List<RechargeWrapper> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, RechargeWrapper rechargeWrapper, int position) {
        holder.setBackgroundRes(R.id.ll_item, R.drawable.content_border_selected);
        holder.setImageResource(R.id.iv_pay_way, rechargeWrapper.getImageRes());
        holder.setText(R.id.tv_pay_way, rechargeWrapper.getContent());
    }

    @Data
    public static class RechargeWrapper {
        int type;
        int imageRes;
        String content;
        boolean isSelected;

        public RechargeWrapper(int type, int imageRes, String content, boolean isSelected) {
            this.type = type;
            this.imageRes = imageRes;
            this.content = content;
            this.isSelected = isSelected;
        }
    }
}
