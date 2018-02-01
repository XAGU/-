package com.xiaolian.amigo.data.network.model.funds;

import java.util.List;

import lombok.Data;

/**
 * 充值提现列表
 *
 * @author zcd
 * @date 17/10/18
 */
@Data
public class QueryFundsListRespDTO {
    private List<FundsInListDTO> funds;
    private Integer total;
}
