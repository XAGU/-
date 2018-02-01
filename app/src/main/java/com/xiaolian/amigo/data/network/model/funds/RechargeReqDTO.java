package com.xiaolian.amigo.data.network.model.funds;

import lombok.Data;

/**
 * 充值reqDTO
 *
 * @author zcd
 * @date 17/10/14
 */
@Data
public class RechargeReqDTO {
    private Long denominationId;
    private Double amount;
    private Integer thirdAccountType;
}
