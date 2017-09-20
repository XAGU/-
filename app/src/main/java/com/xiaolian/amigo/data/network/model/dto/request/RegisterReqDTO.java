package com.xiaolian.amigo.data.network.model.dto.request;

import lombok.Data;

/**
 * 注册接口请求DTO
 *
 * @author zcd
 */
@Data
public class RegisterReqDTO {

    private String code;
    private String mobile;
    private String password;
    private Long schoolId;

    public RegisterReqDTO(String code, String mobile, String password, Long schoolId) {
        this.code = code;
        this.mobile = mobile;
        this.password = password;
        this.schoolId = schoolId;
    }

    public RegisterReqDTO() {
    }
}
