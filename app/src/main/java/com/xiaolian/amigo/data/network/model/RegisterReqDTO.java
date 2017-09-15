package com.xiaolian.amigo.data.network.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 注册接口请求DTO
 * @author zcd
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterReqDTO {
    private String code;
    private int mobile;
    private String password;
    private int schoolId;
}
