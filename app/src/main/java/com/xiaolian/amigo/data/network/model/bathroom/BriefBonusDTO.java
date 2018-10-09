package com.xiaolian.amigo.data.network.model.bathroom;

import lombok.Data;

/**
 * @author zcd
 * @date 18/7/4
 */
@Data
public class BriefBonusDTO {
    /**
     * 代金券id
     */
    private Long id;
    /**
     * 代金券描述
     */
    private String description;
    /**
     * 代金券面额
     */
    private Double amount;
}
