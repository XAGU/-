package com.xiaolian.amigo.data.network.model.dto.request;

import lombok.Data;

/**
 * 获取支付宝app支付订单请求参数
 * <p>
 * Created by zcd on 10/25/17.
 */
@Data
public class AlipayTradeAppPayArgsReqDTO {
    private Long fundsId;
}
