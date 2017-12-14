package com.xiaolian.amigo.data.network.model.funds;

import lombok.Data;

/**
 * 请求充值提现列表
 * <p>
 * Created by zcd on 10/18/17.
 */
@Data
public class QueryPersonalFundsListReqDTO {
    private Integer dealStatus;
    private Integer fundsType;
    private Integer page;
    private Integer size;
}
