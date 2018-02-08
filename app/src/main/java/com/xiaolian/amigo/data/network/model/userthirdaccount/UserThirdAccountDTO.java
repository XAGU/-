package com.xiaolian.amigo.data.network.model.userthirdaccount;

import lombok.Data;

/**
 * 第三方账号列表
 *
 * @author zcd
 * @date 17/10/27
 */
@Data
public class UserThirdAccountDTO {
    private String accountName;
    private Long id;
    private String userRealName;
}
