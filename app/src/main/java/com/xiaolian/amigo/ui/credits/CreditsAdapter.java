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
 * @author zcd
 * @date 18/2/23
 */

public class CreditsAdapter extends CommonAdapter<CreditsAdapter.PointItem> {
    interface OnExchangeClickListener {
        void onExchangeClick(Long bonusId, int deviceType, String bonusAmount, int pointAmount);
    }
    CreditsAdapter(Context context, int layoutId, List<PointItem> datas) {
        super(context, layoutId, datas);
    }

    private OnExchangeClickListener exchangeClickListener;

    void setExchangeClickListener(OnExchangeClickListener listener) {
        this.exchangeClickListener = listener;
    }

    @Override
    protected void convert(ViewHolder holder, PointItem pointItem, int position) {
        holder.setText(R.id.tv_bonus_name, String.format("¥%s%s券", pointItem.getBonusAmount(),
                Device.getDevice(pointItem.getDeviceType()).getDesc()));
        holder.setTextColor(R.id.tv_bonus_name,
                ContextCompat.getColor(holder.getConvertView().getContext(),
                        Device.getDevice(pointItem.getDeviceType()).getColorRes()));
        holder.setText(R.id.tv_point_desc, String.format(Locale.getDefault(),
                "%d积分兑换", pointItem.getPointAmount()));
        holder.getView(R.id.tv_point_desc).setOnClickListener(v -> {
            if (exchangeClickListener != null) {
                exchangeClickListener.onExchangeClick(pointItem.getBonusId(),
                        pointItem.getDeviceType(),
                        pointItem.getBonusAmount(), pointItem.getPointAmount());
            }
        });
    }

    @Data
    public static class PointItem {
        PointItem(Long bonusId, int deviceType, String bonusAmount, int pointAmount) {
            this.bonusId = bonusId;
            this.deviceType = deviceType;
            this.bonusAmount = bonusAmount;
            this.pointAmount = pointAmount;
        }

        private Long bonusId;
        private int deviceType;
        private String bonusAmount;
        private int pointAmount;
    }
}
