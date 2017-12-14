package com.xiaolian.amigo.data.network.model.funds;

import lombok.Data;

/**
 * 充值reqDTO
 * <p>
 * Created by zcd on 10/14/17.
 */
@Data
public class RechargeReqDTO {
    private Long denominationId;
    private Double amount;
    private Integer thirdAccountType;
}
