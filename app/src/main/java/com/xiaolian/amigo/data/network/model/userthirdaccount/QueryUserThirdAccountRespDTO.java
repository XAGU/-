package com.xiaolian.amigo.data.network.model.userthirdaccount;

import java.util.List;

import lombok.Data;

/**
 * 第三方账号列表
 * <p>
 * Created by zcd on 10/27/17.
 */
@Data
public class QueryUserThirdAccountRespDTO {
    private List<UserThirdAccountDTO> thirdAccounts;
}
