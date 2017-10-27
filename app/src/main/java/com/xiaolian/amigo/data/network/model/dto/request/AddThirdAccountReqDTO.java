package com.xiaolian.amigo.data.network.model.dto.request;

import lombok.Data;

/**
 * 新增第三方账号
 * <p>
 * Created by zcd on 10/27/17.
 */
@Data
public class AddThirdAccountReqDTO {
    private String accountName;
    private Integer accountType;
    private String userRealName;
}
