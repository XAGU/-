package com.xiaolian.amigo.data.network.model.dto.response;

import java.util.List;

import lombok.Data;

/**
 * 充值提现列表
 * <p>
 * Created by zcd on 10/18/17.
 */
@Data
public class QueryFundsListRespDTO {
    private List<FundsInListDTO> funds;
    private Integer total;
}
