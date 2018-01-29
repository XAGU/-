package com.xiaolian.amigo.data.network.model.trade;

import lombok.Data;

/**
 * 支付请求
 * <p>
 * Created by caidong on 2017/10/9.
 */
@Data
public class PayReqDTO {

    // 设备mac地址
    private String macAddress;

    // 付款方式（余额支付 or 代金券支付）
//    @NonNull
//    private Integer method;

    // 预付金额
    private Double prepay;

    // 代金券id
    private Long bonusId;

    private Integer mode;

}
