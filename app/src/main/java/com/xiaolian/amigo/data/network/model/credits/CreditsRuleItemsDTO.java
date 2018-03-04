package com.xiaolian.amigo.data.network.model.credits;

import lombok.Data;

/**
 * 获取积分兑换规则
 * @author zcd
 * @date 18/2/26
 */
@Data
public class CreditsRuleItemsDTO {
    private String bonusAmount;
    private Long bonusId;
    /**
     * 积分
     */
    private Integer credits;
    private Integer deviceType;
}
