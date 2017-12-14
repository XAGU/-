package com.xiaolian.amigo.data.network.model.login;

import lombok.Data;

/**
 * 注册接口请求DTO
 *
 * @author zcd
 */
@Data
public class RegisterReqDTO {
    public static final int SYSTEM_CODE = 2;

    private String code;
    private String mobile;
    private String password;
    private Long schoolId;

    private String brand;
    private String model;
    private Integer system;
    private String uniqueId;
    private String systemVersion;
    private String appVersion;

    public RegisterReqDTO(String code, String mobile, String password, Long schoolId) {
        this.code = code;
        this.mobile = mobile;
        this.password = password;
        this.schoolId = schoolId;
    }

    public RegisterReqDTO() {
    }
}
