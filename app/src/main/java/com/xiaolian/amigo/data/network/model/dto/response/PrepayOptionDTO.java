package com.xiaolian.amigo.data.network.model.dto.response;

import java.util.List;

/**
 * 订单预备信息
 * <p>
 * Created by zcd on 10/13/17.
 */

public class PrepayOptionDTO {
    private List<PrepayOptionItemDTO> items;
    private Integer usefor;
}
