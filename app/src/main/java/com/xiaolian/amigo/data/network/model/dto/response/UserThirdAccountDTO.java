package com.xiaolian.amigo.data.network.model.dto.response;

import lombok.Data;

/**
 * 第三方账号列表
 * <p>
 * Created by zcd on 10/27/17.
 */
@Data
public class UserThirdAccountDTO {
    private String accountName;
    private Long id;
    private String userRealName;
}
