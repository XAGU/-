package com.xiaolian.amigo.data.network.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 网络返回-订单详情
 * <p>
 * Created by caidong on 2017/10/11.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailRespDTO {

    // 订单编号
    private String orderNo;

    // 设备地址
    private String location;

    // 找零
    private String odd;

    // 实际消费
    private String consume;

    // 支付方式
    private Integer paymentType;

    // 创建时间
    private Long createTime;

    // 预付金额
    private String prepay;

    // 设备类型
    private Integer deviceType;

}
