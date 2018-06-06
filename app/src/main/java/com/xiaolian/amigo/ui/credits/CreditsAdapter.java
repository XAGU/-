package com.xiaolian.amigo.ui.credits;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.Device;
import com.xiaolian.amigo.util.DimentionUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;
import java.util.Locale;

import lombok.Data;

/**
 * 我的积分页面 积分兑换规则列表adapter
 *
 * @author zcd
 * @date 18/2/23
 */

public class CreditsAdapter extends CommonAdapter<CreditsAdapter.CreditsItem> {
    private Context context;
    interface OnExchangeClickListener {
        void onExchangeClick(Long bonusId, int deviceType, String bonusAmount, int pointAmount);
    }

    CreditsAdapter(Context context, int layoutId, List<CreditsItem> datas) {
        super(context, layoutId, datas);
        this.context = context;
    }

    private OnExchangeClickListener exchangeClickListener;

    void setExchangeClickListener(OnExchangeClickListener listener) {
        this.exchangeClickListener = listener;
    }

    @Override
    protected void convert(ViewHolder holder, CreditsItem creditsItem, int position) {
        setCreditExchangeAmount(holder.getView(R.id.tv_bonus_name), creditsItem.getBonusAmount(),
                Device.getDevice(creditsItem.getDeviceType()).getDesc() + "券");
        holder.setBackgroundRes(R.id.ll_credit_item, DeviceSupport.getDeviceCreditBg(creditsItem.getDeviceType()));
        holder.setText(R.id.tv_point_desc, String.format(Locale.getDefault(),
                "%d积分兑换", creditsItem.getCreditsAmount()));
        holder.setTextColor(R.id.tv_point_desc,
                ContextCompat.getColor(context,
                        DeviceSupport.getDeviceCreditExchangeColor(creditsItem.getDeviceType())));
        holder.getView(R.id.tv_point_desc).setOnClickListener(v -> {
            if (exchangeClickListener != null) {
                exchangeClickListener.onExchangeClick(creditsItem.getBonusId(),
                        creditsItem.getDeviceType(),
                        creditsItem.getBonusAmount(), creditsItem.getCreditsAmount());
            }
        });
    }

    private void setCreditExchangeAmount(TextView textView, String amount, String desc) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        SpannableString amountSpan = new SpannableString(String.format("¥%s", amount));
        amountSpan.setSpan(new AbsoluteSizeSpan(
                        DimentionUtils.convertSpToPixels(28, context)), 0, amountSpan.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        amountSpan.setSpan(new StyleSpan(Typeface.BOLD), 0, amountSpan.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(amountSpan);
        SpannableString descSpan = new SpannableString(desc);
        descSpan.setSpan(new AbsoluteSizeSpan(
                        DimentionUtils.convertSpToPixels(12, context)), 0, descSpan.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        descSpan.setSpan(new StyleSpan(Typeface.NORMAL), 0, descSpan.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(descSpan);
        textView.setText(builder);
    }

    /**
     * 积分兑换列表项
     */
    @Data
    public static class CreditsItem {
        CreditsItem(Long bonusId, int deviceType, String bonusAmount, int creditsAmount) {
            this.bonusId = bonusId;
            this.deviceType = deviceType;
            this.bonusAmount = bonusAmount;
            this.creditsAmount = creditsAmount;
        }

        private Long bonusId;
        private int deviceType;
        private String bonusAmount;
        /**
         * 积分数量
         */
        private int creditsAmount;
    }

    private static final class DeviceSupport {
        static int getDeviceCreditBg(Integer type) {
            if (type == null) {
                return 0;
            }
            switch (Device.getDevice(type)) {
                case DRYER:
                    return R.drawable.bg_credit_dryer;
                case HEATER:
                    return R.drawable.bg_credit_heater;
                case WASHER:
                    return R.drawable.bg_credit_washer;
                case DISPENSER:
                    return R.drawable.bg_credit_dispenser;
            }
            return 0;
        }

        static int getDeviceCreditExchangeColor(Integer type) {
            if (type == null) {
                return 0;
            }
            switch (Device.getDevice(type)) {
                case DRYER:
                    return R.color.credit_dryer;
                case HEATER:
                    return R.color.credit_heater;
                case WASHER:
                    return R.color.credit_washer;
                case DISPENSER:
                    return R.color.credit_dispenser;
            }
            return 0;
        }
    }
}
