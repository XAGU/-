package com.xiaolian.amigo.data.network.model.dto.request;

import lombok.Data;

/**
 * 投诉查重
 * <p>
 * Created by zcd on 17/11/10.
 */
@Data
public class CheckComplaintReqDTO {
    private Long orderId;
    private Integer orderType;
}
