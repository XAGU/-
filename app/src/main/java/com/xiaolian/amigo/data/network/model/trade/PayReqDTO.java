package com.xiaolian.amigo.data.network.model.trade;

import lombok.Data;

/**
 * 支付请求
 *
 * @author caidong
 * @date 17/10/9
 */
@Data
public class PayReqDTO {

    /**
     * 设备mac地址
     */
    private String macAddress;

    /**
     * 预付金额
     */
    private Double prepay;

    /**
     * 代金券id
     */
    private Long bonusId;

    private Integer mode;

}
