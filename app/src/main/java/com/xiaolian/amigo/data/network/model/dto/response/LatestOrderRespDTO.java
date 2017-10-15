package com.xiaolian.amigo.data.network.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by caidong on 2017/10/15.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LatestOrderRespDTO {

    // 订单id
    private Long orderId;

    // 订单产生时间
    private Long finishTime;

}