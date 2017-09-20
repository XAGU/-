package com.xiaolian.amigo.data.network.model.dto.request;

import lombok.Data;

/**
 * 校验验证码
 * <p>
 * Created by zcd on 9/20/17.
 */
@Data
public class VerificationCodeCheckReqDTO {
    private String code;
    private String mobile;
}
