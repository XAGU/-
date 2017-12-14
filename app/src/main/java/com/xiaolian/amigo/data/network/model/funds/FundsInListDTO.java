package com.xiaolian.amigo.data.network.model.funds;

import lombok.Data;

/**
 * 充值提现
 * <p>
 * Created by zcd on 10/18/17.
 */
@Data
public class FundsInListDTO {
    private String amount;
    private Long createTime;
    private Long id;
    private Integer operationType;
    private String orderNo;
    private Integer status;
    private Long userId;
}
