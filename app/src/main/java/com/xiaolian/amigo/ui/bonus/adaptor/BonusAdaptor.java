package com.xiaolian.amigo.ui.bonus.adaptor;

import android.content.Context;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.network.model.bonus.Bonus;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import lombok.Data;

/**
 * BonusAdaptor
 * @author zcd
 */
public class BonusAdaptor extends CommonAdapter<BonusAdaptor.BonusWrapper> {


    public BonusAdaptor(Context context, int layout, List<BonusWrapper> bonuses) {
        super(context, layout, bonuses);
    }

    @Override
    protected void convert(ViewHolder holder, BonusWrapper bonusWrapper, int position) {
        holder.setText(R.id.tv_amount, "￥" + bonusWrapper.amount.toString());
        holder.setText(R.id.tv_type, bonusWrapper.type.toString());
        holder.setText(R.id.tv_time_end, bonusWrapper.timeEnd);
        holder.setText(R.id.tv_desc, bonusWrapper.desc);
        holder.setText(R.id.tv_time_left, "剩" + bonusWrapper.timeLeft.toString() + "日");
    }

    @Data
    public static class BonusWrapper {
        // 红包类型
        Integer type;
        // 红包金额
        Long amount;
        // 到期时间
        String timeEnd;
        // 描述信息
        String desc;
        // 剩余时间
        Long timeLeft;

        public BonusWrapper(Integer type, Long amount, String timeEnd, String desc, Long timeLeft) {
            this.type = type;
            this.amount = amount;
            this.timeEnd = timeEnd;
            this.desc = desc;
            this.timeLeft = timeLeft;
        }

        public BonusWrapper(Bonus bonus) {
            this.type = bonus.getDeviceType();
            this.amount = bonus.getAmount();
            this.timeEnd = bonus.getEndTime();
            this.desc = bonus.getRemarks();
            this.timeLeft = bonus.getTimeLimit();
        }
    }



}
