package com.xiaolian.amigo.data.network.model.wallet;

import lombok.Data;

/**
 * 充值金额
 * <p>
 * Created by zcd on 9/20/17.
 */
@Data
public class RechargeDenominations {
    private Integer activityType;
    private Long amount;
    private Long id;
    private Long value;
}
