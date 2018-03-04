package com.xiaolian.amigo.data.network.model.credits;

import lombok.Data;

/**
 * 兑换积分
 * @author zcd
 * @date 18/2/26
 */
@Data
public class CreditsExchangeReqDTO {
    private Long bonusId;
    /**
     * 积分
     */
    private Integer credits;
    private Integer deviceType;
}
