package com.xiaolian.amigo.ui.wallet.adaptor;

import android.content.Context;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.enumeration.Device;
import com.xiaolian.amigo.data.network.model.order.Order;
import com.xiaolian.amigo.util.CommonUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * <p>
 * Created by zcd on 10/10/17.
 */

public class PrepayAdaptor extends CommonAdapter<PrepayAdaptor.OrderWrapper> {

    public PrepayAdaptor(Context context, int layoutId, List<OrderWrapper> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, OrderWrapper orderWrapper, int position) {
        holder.setText(R.id.tv_prepay_device, orderWrapper.getDevice());
        holder.setText(R.id.tv_prepay_time, CommonUtil.stampToDate(orderWrapper.getTime()));
        holder.setText(R.id.tv_prepay_amount, orderWrapper.getPrepay());
        if (orderWrapper.getStatus() == 1) {
            holder.setText(R.id.tv_prepay_title, "待找零");
        } else {
            holder.setText(R.id.tv_prepay_title, "已结束");
        }
    }

    @Data
    public static class OrderWrapper implements Serializable {
        // 设备类型
        Integer type;
        // 设备
        String device;
        // 时间
        Long time;
        // 预付金额
        String prepay;
        // 状态
        Integer status;
        // 原始订单内容，供查询订单详情时使用
        Order order;

        public OrderWrapper(Order order) {
            this.order = order;
            this.type = order.getDeviceType();
            if (Device.getDevice(order.getDeviceType()) != null) {
                this.device = Device.getDevice(order.getDeviceType()).getDesc() + "：" + order.getDeviceNo();
            } else {
                this.device = "未知设备";
            }
            this.time = order.getCreateTime();
            this.prepay = order.getPrepay();
            this.status = order.getStatus();
        }
    }
}
