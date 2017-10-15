package com.xiaolian.amigo.data.network.model.dto.response;

import lombok.Data;

/**
 * 订单预备信息
 * <p>
 * Created by zcd on 10/13/17.
 */
@Data
public class PrepayOptionItemDTO {
    private String description;
    private Double prepay;
}
