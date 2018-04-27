package com.xiaolian.amigo.data.network.model.wxpay;

import lombok.Data;

/**
 * 解析app支付结果
 *
 * @author zcd
 * @date 17/10/25
 */
@Data
public class WxpayTradeAppPayResultParseReqDTO {
    private Long fundsId;
    private Integer result;
}
