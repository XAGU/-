package com.xiaolian.amigo.ui.bonus.adaptor;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.Device;
import com.xiaolian.amigo.data.vo.Bonus;
import com.xiaolian.amigo.util.TimeUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.io.Serializable;
import java.text.DecimalFormat;
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

    private DecimalFormat df = new DecimalFormat("###.##");

    private Context context;

    public BonusAdaptor(Context context, int layout, List<BonusWrapper> bonuses) {
        super(context, layout, bonuses);
        this.context = context;
    }

    @Override
    protected void convert(ViewHolder holder, BonusWrapper bonusWrapper, int position) {
        holder.setText(R.id.tv_amount, "¥" + df.format(bonusWrapper.getAmount()));
        holder.setBackgroundRes(R.id.tv_amount, Support.getBgByDeviceType(bonusWrapper.getType()));
        holder.setImageResource(R.id.iv_left, Support.getLeftImageByDeviceType(bonusWrapper.getType()));
        holder.setTextColor(R.id.tv_type,
                ContextCompat.getColor(context, Device.getDevice(bonusWrapper.getType()).getColorRes()));
//        holder.setText(R.id.tv_amount, String.format(Locale.getDefault(), "¥%.0f", bonusWrapper.amount));
        holder.setText(R.id.tv_type, bonusWrapper.getName());
        if (bonusWrapper.getStartTime() != null && bonusWrapper.getTimeEnd() != null) {
            holder.setText(R.id.tv_time_end, "有效期" + TimeUtils.millis2String(bonusWrapper.getStartTime(),
                    TimeUtils.MY_DATE_FORMAT3) + "至" + TimeUtils.millis2String(bonusWrapper.getTimeEnd(),
                    TimeUtils.MY_DATE_FORMAT3));
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

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public Double getAmount() {
            return amount;
        }

        public void setAmount(Double amount) {
            this.amount = amount;
        }

        public Long getTimeEnd() {
            return timeEnd;
        }

        public void setTimeEnd(Long timeEnd) {
            this.timeEnd = timeEnd;
        }

        public Long getStartTime() {
            return startTime;
        }

        public void setStartTime(Long startTime) {
            this.startTime = startTime;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public Long getTimeLeft() {
            return timeLeft;
        }

        public void setTimeLeft(Long timeLeft) {
            this.timeLeft = timeLeft;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    private static final class Support {
        static int getBgByDeviceType(Integer type) {
            if (type == null) {
                return 0;
            }
            switch (Device.getDevice(type)) {
                case DRYER:
                    return R.drawable.bg_bonus_dryer;
                case HEATER:
                    return R.drawable.bg_bonus_heater;
                case WASHER:
                    return R.drawable.bg_bonus_washer;
                case DISPENSER:
                    return R.drawable.bg_bonus_dispenser;
                case DRYER2:
                    return R.drawable.bg_bonus_dryer2 ;
            }
            return R.drawable.bg_bonus_heater;
        }

        static int getLeftImageByDeviceType(Integer type) {
            if (type == null) {
                return 0;
            }
            switch (Device.getDevice(type)) {
                case DRYER:
                    return R.drawable.bg_bonus_left_dryer;
                case HEATER:
                    return R.drawable.bg_bonus_left_heater;
                case WASHER:
                    return R.drawable.bg_bonus_left_washer;
                case DISPENSER:
                    return R.drawable.bg_bonus_left_dispenser;
                case DRYER2:
                    return R.drawable.bg_bonus_left_dryer2;
            }
            return R.drawable.bg_bonus_left_heater;
        }
    }
}
