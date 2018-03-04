package com.xiaolian.amigo.ui.credits;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.Device;
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
    interface OnExchangeClickListener {
        void onExchangeClick(Long bonusId, int deviceType, String bonusAmount, int pointAmount);
    }

    CreditsAdapter(Context context, int layoutId, List<CreditsItem> datas) {
        super(context, layoutId, datas);
    }

    private OnExchangeClickListener exchangeClickListener;

    void setExchangeClickListener(OnExchangeClickListener listener) {
        this.exchangeClickListener = listener;
    }

    @Override
    protected void convert(ViewHolder holder, CreditsItem creditsItem, int position) {
        holder.setText(R.id.tv_bonus_name, String.format("¥%s%s券", creditsItem.getBonusAmount(),
                Device.getDevice(creditsItem.getDeviceType()).getDesc()));
        holder.setTextColor(R.id.tv_bonus_name,
                ContextCompat.getColor(holder.getConvertView().getContext(),
                        Device.getDevice(creditsItem.getDeviceType()).getColorRes()));
        holder.setText(R.id.tv_point_desc, String.format(Locale.getDefault(),
                "%d积分兑换", creditsItem.getCreditsAmount()));
        holder.getView(R.id.tv_point_desc).setOnClickListener(v -> {
            if (exchangeClickListener != null) {
                exchangeClickListener.onExchangeClick(creditsItem.getBonusId(),
                        creditsItem.getDeviceType(),
                        creditsItem.getBonusAmount(), creditsItem.getCreditsAmount());
            }
        });
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
}
