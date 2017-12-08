package com.xiaolian.amigo.data.network.model.dto.request;

import lombok.Data;

/**
 * 解析app支付结果
 * <p>
 * Created by zcd on 10/25/17.
 */
@Data
public class AlipayTradeAppPayResultParseReqDTO {
    private String resultStatus;
    private String result;
    private String memo;
    private Long fundsId;
}
