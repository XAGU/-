package com.xiaolian.amigo.data.network.model.dto.response;

import lombok.Data;

/**
 * 解析app支付结果
 * <p>
 * Created by zcd on 10/25/17.
 */
@Data
public class AlipayTradeAppPayResultParseRespDTO {
    private Integer code;
    private String msg;
}
