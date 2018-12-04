package com.xiaolian.amigo.data.network.model.login;

import lombok.Data;

/**
 * @author zcd
 * @date 17/12/14
 */
@Data
public class LoginRespDTO {

    //alipay login 属于支付宝登录字段
    private Long alipayUserId;
    private boolean isBinding;
    private String failReason;
    //end

    //密码保护
    private Integer protectInMinutes;
    private Integer remaining;
    private Boolean result;
   //密码保护end

    //wechat login begin

    private String openId;
    private Boolean bound;
    private String unionId;
    private Boolean wechatBound;

    //wechat login end

    private String accessToken;
    private String refreshToken ;
    private EntireUserDTO user;
}
