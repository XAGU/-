package com.xiaolian.amigo.ui.point;

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

public class PointAdapter extends CommonAdapter<PointAdapter.PointItem> {
    interface OnExchangeClickListener {
        void onExchangeClick(int deviceType, String bonusAmount, int pointAmount);
    }
    PointAdapter(Context context, int layoutId, List<PointItem> datas) {
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
                exchangeClickListener.onExchangeClick(pointItem.getDeviceType(),
                        pointItem.getBonusAmount(), pointItem.getPointAmount());
            }
        });
    }

    @Data
    static class PointItem {
        PointItem(int deviceType, String bonusAmount, int pointAmount) {
            this.deviceType = deviceType;
            this.bonusAmount = bonusAmount;
            this.pointAmount = pointAmount;
        }

        private int deviceType;
        private String bonusAmount;
        private int pointAmount;
    }
}
