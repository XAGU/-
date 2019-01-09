package com.xiaolian.amigo.data.network.model.funds;

import java.util.List;

import lombok.Data;

@Data
public class QueryWithdrawTypeListRespDTO {

    /**
     * 类型
     */
    private List<Integer> withdrawTypes ;

    /**
     * 总额
     */
    private int total ;
}
