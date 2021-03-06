package com.xiaolian.amigo.data.network.model.userbill;

import lombok.Data;

/**
 * @author zcd
 * @date 18/6/4
 */
@Data
public class UserMonthlyBillConsumeRespDTO {
    /**
     * 消费类型对应消费额
     */
    private Double consume;
    /**
     * 消费类型
     */
    private Integer consumeType;
    /**
     * 消费类型名称
     */
    private String consumeTypeName;
}
