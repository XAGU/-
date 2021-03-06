package com.xiaolian.amigo.data.network.model.funds;

import lombok.Data;

/**
 * 请求充值提现列表
 *
 * @author zcd
 * @date 17/10/18
 */
@Data
public class QueryPersonalFundsListReqDTO {
    private Integer dealStatus;
    /**
     * 类型 WITHDRAW(1),RECHARGE(2)
     */
    private Integer fundsType;
    private Integer page;
    private Integer size;
    private Integer month;
    private Integer year;
    /**
     * 状态:PENDING(1), FINISHED(2)
     */
    private Integer status;
}
