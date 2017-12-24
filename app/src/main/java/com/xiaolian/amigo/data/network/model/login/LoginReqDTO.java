package com.xiaolian.amigo.data.network.model.login;

import lombok.Data;

/**
 * 登录
 * <p>
 * Created by zcd on 9/20/17.
 */
@Data
public class LoginReqDTO {
    private String mobile;
    private String password;
    private String brand;
    private String model;
    private Integer system;
    private String uniqueId;
    private String systemVersion;
    private String appVersion;
}
