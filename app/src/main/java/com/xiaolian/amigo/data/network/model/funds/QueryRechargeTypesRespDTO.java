package com.xiaolian.amigo.data.network.model.funds;

import java.util.List;

import lombok.Data;

/**
 * 充值账号类型
 *
 * @author zcd
 * @date 17/9/20
 */
@Data
public class QueryRechargeTypesRespDTO {
    List<Integer> rechargeTypes;
    Integer total;
}
