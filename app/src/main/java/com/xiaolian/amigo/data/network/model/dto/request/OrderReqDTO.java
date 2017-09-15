package com.xiaolian.amigo.data.network.model.dto.request;

/**
 * 网络请求-订单
 * <p>
 * Created by caidong on 2017/9/15.
 */
public class OrderReqDTO {

    // 订单状态 1 - 使用中， 2 - 已结束
    private int orderStatus;
    // 页码
    private int page;
    // 页数
    private int size;

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
