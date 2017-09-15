package com.xiaolian.amigo.data.network.model.dto.request;

import lombok.Data;

/**
 * 更新用户密码DTO
 * @author zcd
 */
@Data
public class PasswordUpdateReqDTO {
    private String newPassword;
    private String oldPassword;
}
