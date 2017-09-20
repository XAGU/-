package com.xiaolian.amigo.data.network.model.dto.response;

import com.xiaolian.amigo.data.network.model.order.Order;

import java.util.List;

/**
 * 网络返回 - 订单
 * <p>
 * Created by caidong on 2017/9/15.
 */
public class OrderRespDTO {

    // 订单总数
    private Integer total;
    // 订单列表
    private List<Order> orders;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
