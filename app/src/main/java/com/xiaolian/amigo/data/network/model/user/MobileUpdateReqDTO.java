package com.xiaolian.amigo.data.network.model.user;

import lombok.Data;

/**
 * 更新用户手机号DTO
 *
 * @author zcd
 * @date 17/9/15
 */
@Data
public class MobileUpdateReqDTO {
    private String code;
    private String mobile;
}
