package com.xiaolian.amigo.data.network.model.funds;

import lombok.Data;

@Data
public class WechatWithdrawReqDTO {

    /**
     * 提现金额
     */
    private float amount ;

    /**
     * 应用类型：1-原生； 2-小程序
     */
    private int appSource ;

    /**
     * 微信临时授权码
     */
    private String code ;

    /**
     * 用户真实姓名
     */
    private String userRealName ;
}
