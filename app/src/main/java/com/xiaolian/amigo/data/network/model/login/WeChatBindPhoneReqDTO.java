package com.xiaolian.amigo.data.network.model.login;

import lombok.Data;

@Data
public class WeChatBindPhoneReqDTO {
    String openId;

    String mobile;
    //验证码
    String code;

    String unionId;

    Integer edition;//用户登录使用app版本（用户登录使用app版本（1：笑联 2:xiaook企业版 3:笑联企业版）

    Integer appSource;//1 - 原生; 2 - 小程序

    String uniqueId;
}
