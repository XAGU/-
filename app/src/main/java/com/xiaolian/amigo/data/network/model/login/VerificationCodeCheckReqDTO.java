package com.xiaolian.amigo.data.network.model.login;

import lombok.Data;

/**
 * 校验验证码
 *
 * @author zcd
 * @date 17/9/20
 */
@Data
public class VerificationCodeCheckReqDTO {
    private String code;
    private String mobile;
}
