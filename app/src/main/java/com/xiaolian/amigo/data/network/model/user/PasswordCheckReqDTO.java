package com.xiaolian.amigo.data.network.model.user;

import lombok.Data;

/**
 * 校验密码
 *
 * @author zcd
 * @date 17/9/25
 */
@Data
public class PasswordCheckReqDTO {
    private String password;
}
