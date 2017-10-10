package com.xiaolian.amigo.ui.wallet.adaptor;

import android.content.Context;

import com.xiaolian.amigo.data.enumeration.Device;
import com.xiaolian.amigo.data.network.model.order.Order;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

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

    }

    public static class OrderWrapper {
        // 设备类型
        Integer type;
        // 设备
        String device;
        // 时间
        Long time;
        // 预付金额
        Double amount;
        // 原始订单内容，供查询订单详情时使用
        Order order;

        public OrderWrapper(Order order) {
            this.order = order;
            this.type = order.getDeviceType();
            this.device = Device.getDevice(order.getDeviceType()).getDesc() + "：" + order.getDeviceNo();
            this.time = order.getCreateTime();
            this.amount = order.getPrepay();
        }
    }
}
