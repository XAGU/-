package com.xiaolian.amigo.data.network.model.order;

import com.xiaolian.amigo.data.network.model.order.Order;

import java.util.List;

import lombok.Data;

/**
 * 网络返回 - 订单
 * <p>
 * Created by caidong on 2017/9/15.
 */
@Data
public class OrderRespDTO {

    // 订单总数
    private Integer total;
    // 订单列表
    private List<OrderInListDTO> orders;
}
