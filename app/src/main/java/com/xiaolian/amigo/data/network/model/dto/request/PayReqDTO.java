package com.xiaolian.amigo.data.network.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * 支付请求
 * <p>
 * Created by caidong on 2017/10/9.
 */
@Data
public class PayReqDTO {

    // 设备mac地址
    private String macAddress;

    // 付款方式（余额支付 or 红包支付）
//    @NonNull
//    private Integer method;

    // 预付金额
    private Double prepay;

    // 红包id
    private Long bonusId;

}
