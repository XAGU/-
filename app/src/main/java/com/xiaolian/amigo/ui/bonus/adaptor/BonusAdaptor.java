package com.xiaolian.amigo.ui.bonus.adaptor;

import android.content.Context;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.vo.Bonus;
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
 * @date 17/9/18
 */
public class BonusAdaptor extends CommonAdapter<BonusAdaptor.BonusWrapper> {

    private static final int MAX_TIME_LIFT = 3;

    public BonusAdaptor(Context context, int layout, List<BonusWrapper> bonuses) {
        super(context, layout, bonuses);
    }

    @Override
    protected void convert(ViewHolder holder, BonusWrapper bonusWrapper, int position) {
        holder.setText(R.id.tv_amount, String.format(Locale.getDefault(), "¥%.0f", bonusWrapper.amount));
        holder.setText(R.id.tv_type, bonusWrapper.getName());
        if (bonusWrapper.getStartTime() != null && bonusWrapper.getTimeEnd() != null) {
            holder.setText(R.id.tv_time_end, "有效期" + TimeUtils.millis2String(bonusWrapper.getStartTime(),
                    TimeUtils.MY_DATE_FORMAT2) + "至" + TimeUtils.millis2String(bonusWrapper.getTimeEnd(),
                    TimeUtils.MY_DATE_FORMAT2));
        }
        holder.setText(R.id.tv_desc, bonusWrapper.desc);
        if (bonusWrapper.timeLeft != null && bonusWrapper.timeLeft <= MAX_TIME_LIFT) {
            if (bonusWrapper.timeLeft == 0) {
                holder.setText(R.id.tv_time_left, "今日截止");
            } else {
                holder.setText(R.id.tv_time_left, "剩" + bonusWrapper.timeLeft + "日");
            }
        } else {
            holder.setText(R.id.tv_time_left, "");
        }
    }

    @Data
    public static class BonusWrapper implements Serializable {
        /**
         * 代金券类型
         */
        Integer type;
        /**
         * 代金券金额
         */
        Double amount;
        /**
         * 到期时间
         */
        Long timeEnd;
        /**
         * 开始时间
         */
        Long startTime;
        /**
         * 描述信息
         */
        String desc;
        /**
         * 剩余时间
         */
        Long timeLeft;
        /**
         * 名称
         */
        String name;
        /**
         * 代金券id
         */
        Long id;
        /**
         * 备注
         */
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
