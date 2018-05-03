package com.xiaolian.amigo.data.network.model.wxpay;

import com.xiaolian.amigo.util.PayUtil;

import lombok.Data;

/**
 * @author zcd
 * @date 18/4/17
 */
@Data
public class WxpayTradeAppPayArgsRespDTO implements PayUtil.IWeChatPayReq {
    private String appId;
    private String partnerId;
    private String prepayId;
    private String nonceStr;
    private String timeStamp;
    private String packageValue;
    private String sign;
    private String signType;
}
