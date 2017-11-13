package com.xiaolian.amigo.ui.bonus.adaptor;

import android.content.Context;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.network.model.bonus.Bonus;
import com.xiaolian.amigo.util.TimeUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import lombok.Data;

/**
 * BonusAdaptor
 *
 * @author zcd
 */
public class BonusAdaptor extends CommonAdapter<BonusAdaptor.BonusWrapper> {


    public BonusAdaptor(Context context, int layout, List<BonusWrapper> bonuses) {
        super(context, layout, bonuses);
    }

    @Override
    protected void convert(ViewHolder holder, BonusWrapper bonusWrapper, int position) {
        holder.setText(R.id.tv_amount, String.format(Locale.getDefault(), "¥%.0f", bonusWrapper.amount));
        holder.setText(R.id.tv_type, bonusWrapper.getName());
        holder.setText(R.id.tv_time_end, "有效期" + TimeUtils.millis2String(bonusWrapper.getStartTime(),
                TimeUtils.MY_DATE_FORMAT2) + "至" + TimeUtils.millis2String(bonusWrapper.getTimeEnd(),
                TimeUtils.MY_DATE_FORMAT2) );
        holder.setText(R.id.tv_desc, bonusWrapper.desc);
        if (bonusWrapper.timeLeft != null && bonusWrapper.timeLeft <= 3) {
            holder.setText(R.id.tv_time_left, "剩" + String.valueOf(bonusWrapper.timeLeft) + "日");
        } else {
            holder.setText(R.id.tv_time_left, "");
        }
    }

    @Data
    public static class BonusWrapper implements Serializable {
        // 红包类型
        Integer type;
        // 红包金额
        Double amount;
        // 到期时间
        Long timeEnd;
        // 开始时间
        Long startTime;
        // 描述信息
        String desc;
        // 剩余时间
        Long timeLeft;
        // 红包名称
        String name;
        // 红包id
        Long id;
        // 备注
        String remark;
        String description;

        public BonusWrapper(Bonus bonus) {
            this.id = bonus.getId();
            this.type = bonus.getDeviceType();
            this.amount = bonus.getAmount();
            this.timeEnd = bonus.getEndTime();
            this.startTime = bonus.getStartTime();
            this.desc = bonus.getRemarks();
            this.timeLeft = bonus.getTimeLimit();
            this.name = bonus.getName();
            this.remark = bonus.getRemarks();
            this.description = bonus.getDescription();
        }
    }
}
