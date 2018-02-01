package com.xiaolian.amigo.data.network.model.userthirdaccount;

import lombok.Data;

/**
 * 新增第三方账号
 *
 * @author zcd
 * @date 17/10/27
 */
@Data
public class AddThirdAccountReqDTO {
    private String accountName;
    private Integer accountType;
    private String userRealName;
}
