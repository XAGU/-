package com.xiaolian.amigo.data.network.model.alipay;

import lombok.Data;

@Data
public class AlipayBindReq {
    //三方登录code
    String authCode;
    Long alipayUserId;
    String mobile;
    String password;
    //手机验证码
    Integer edition;
    String code;
    Integer schoolId;
}
