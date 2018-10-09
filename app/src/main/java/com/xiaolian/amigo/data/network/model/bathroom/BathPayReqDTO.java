package com.xiaolian.amigo.data.network.model.bathroom;

import lombok.Data;

/**
 * @author zcd
 * @date 18/7/4
 */
@Data
public class BathPayReqDTO {
    private Double amount;
    private Long bookingId;
    /**
     * 1 预约 2 购买编码
     */
    private Integer type;
}
