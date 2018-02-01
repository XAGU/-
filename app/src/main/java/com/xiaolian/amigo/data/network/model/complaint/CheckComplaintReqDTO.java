package com.xiaolian.amigo.data.network.model.complaint;

import lombok.Data;

/**
 * 投诉查重
 *
 * @author zcd
 * @date 17/11/10
 */
@Data
public class CheckComplaintReqDTO {
    private Long orderId;
    private Integer orderType;
}
