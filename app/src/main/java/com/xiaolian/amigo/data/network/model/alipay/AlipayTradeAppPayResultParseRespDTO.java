package com.xiaolian.amigo.data.network.model.alipay;

import lombok.Data;

/**
 * 解析app支付结果
 *
 * @author zcd
 * @date 17/10/25
 */
@Data
public class AlipayTradeAppPayResultParseRespDTO {
    private Integer code;
    private String msg;
}
