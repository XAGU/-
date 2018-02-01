package com.xiaolian.amigo.data.network.model.order;

import lombok.Data;

/**
 * 网络请求-订单
 *
 * @author caidong
 * @date 17/9/15
 */
@Data
public class OrderReqDTO {
    /**
     * 订单状态 1 - 使用中， 2 - 已结束
     */
    private Integer orderStatus;
    /**
     * 页码
     */
    private Integer page;
    /**
     * 页大小
     */
    private Integer size;
    /**
     * 订单类型 1 余额支付 2 代金券支付
     */
    private Integer paymentType;

}
