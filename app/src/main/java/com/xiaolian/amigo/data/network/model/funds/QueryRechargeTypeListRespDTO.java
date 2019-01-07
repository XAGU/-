package com.xiaolian.amigo.data.network.model.funds;

import java.util.List;

import lombok.Data;

@Data
public class QueryRechargeTypeListRespDTO {
    /**
     * 类型
     */
    private List<Integer> rechargeTypes ;

    /**
     * 总额
     */
    private int total ;

}
