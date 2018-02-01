package com.xiaolian.amigo.data.network.model.wallet;

import lombok.Data;

/**
 * 充值金额
 *
 * @author zcd
 * @date 17/9/20
 */
@Data
public class RechargeDenominations {
    private Integer activityType;
    private Double amount;
    private Long id;
    private Double value;
}
