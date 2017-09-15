package com.xiaolian.amigo.data.network.model.dto.request;

import lombok.Data;

/**
 * 更新用户手机号DTO
 * @author zcd
 */
@Data
public class MobileUpdateReqDTO {
    private String code;
    private int mobile;
}
