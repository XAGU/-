package com.xiaolian.amigo.data.network.model.login;

import lombok.Data;

/**
 * 密码重置
 *
 * @author zcd
 * @date 17/9/20
 */
@Data
public class PasswordResetReqDTO {
    private String code;
    private String mobile;
    private String password;
}
