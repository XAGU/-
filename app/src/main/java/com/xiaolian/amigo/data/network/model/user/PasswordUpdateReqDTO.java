package com.xiaolian.amigo.data.network.model.user;

import lombok.Data;

/**
 * 更新用户密码DTO
 *
 * @author zcd
 * @date 17/9/15
 */
@Data
public class PasswordUpdateReqDTO {
    private String newPassword;
    private String oldPassword;
}
