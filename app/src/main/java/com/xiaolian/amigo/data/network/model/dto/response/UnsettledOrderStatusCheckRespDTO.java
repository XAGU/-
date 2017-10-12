package com.xiaolian.amigo.data.network.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by caidong on 2017/10/12.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnsettledOrderStatusCheckRespDTO {

    // 订单id
    private Long orderId;

    // 支付方式
    private Integer paymentType;

    // 描述信息
    private String extra;

    // 未结账单是否在三小时之内
    private boolean lessThanThreeHours;

    // 订单状态
    private Integer status;

}
