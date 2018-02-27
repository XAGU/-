package com.xiaolian.amigo.data.network.model.credits;

import java.util.List;

import lombok.Data;

/**
 * 获取积分兑换规则
 * @author zcd
 * @date 18/2/26
 */
@Data
public class CreditsRuleRespDTO {
    private List<CreditsRuleItemsDTO> items;
}
