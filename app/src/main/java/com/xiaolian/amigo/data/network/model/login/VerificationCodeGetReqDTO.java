package com.xiaolian.amigo.data.network.model.login;

import lombok.Data;

/**
 * 验证码
 *
 * @author zcd
 * @date 17/9/18
 */

@Data
public class VerificationCodeGetReqDTO {
    private String mobile;
}
