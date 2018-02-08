package com.xiaolian.amigo.data.network.model.userthirdaccount;

import java.util.List;

import lombok.Data;

/**
 * 第三方账号列表
 *
 * @author zcd
 * @date 17/10/27
 */
@Data
public class QueryUserThirdAccountRespDTO {
    private List<UserThirdAccountDTO> thirdAccounts;
}
