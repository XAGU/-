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
 * 预付列表adapter
 *
 * @author zcd
 * @date 17/10/10
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
        /**
         * 设备类型
         */
        private Integer type;
        /**
         * 设备
         */
        private String device;
        /**
         * 时间
         */
        private Long time;
        /**
         * 预付金额
         */
        private String prepay;
        /**
         * 状态
         */
        private Integer status;
        /**
         * 原始订单内容，供查询订单详情时使用
         */
        private Order order;
        /**
         * 设备mac地址
         */
        private String macAddress;
        /**
         * 设备地址
         */
        private String location;
        private Long residenceId;
        /**
         * 供应商id
         */
        private Long supplierId;

        public OrderWrapper(Order order) {
            this.order = order;
            this.type = order.getDeviceType();
            this.supplierId = order.getSupplierId();
            if (Device.getDevice(order.getDeviceType()) != null) {
                this.device = Device.getDevice(order.getDeviceType()).getDesc() + "：" + order.getLocation();
                this.macAddress = order.getMacAddress();
                this.location = order.getLocation();
            } else {
                this.device = "未知设备";
            }
            this.time = order.getCreateTime();
            this.prepay = order.getPrepay();
            this.status = order.getStatus();
            this.residenceId = order.getResidenceId();
        }
    }
}
