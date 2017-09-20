package com.xiaolian.amigo.data.network.model.dto.request;

import lombok.Data;

/**
 * 密码重置
 * <p>
 * Created by zcd on 9/20/17.
 */
@Data
public class PasswordResetReqDTO {
    private String code;
    private String mobile;
    private String password;
}
