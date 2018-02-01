package com.xiaolian.amigo.data.network.model.funds;

import lombok.Data;

/**
 * 充值提现
 *
 * @author zcd
 * @date 17/10/18
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
    private Boolean instead;
}
