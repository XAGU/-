package com.xiaolian.amigo.data.network.model.login;

import lombok.Data;

@Data
public class WeChatResiterReqDTO {

    String code;
    String mobile;
    String password;
    String unionId;
    String openId;
    Integer schoolId;

    Integer edition;//用户登录使用app版本（用户登录使用app版本（1：笑联 2:xiaook企业版 3:笑联企业版）

    Integer appSource;//1 - 原生; 2 - 小程序
}
