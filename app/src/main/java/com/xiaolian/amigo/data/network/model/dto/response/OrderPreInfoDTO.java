package com.xiaolian.amigo.data.network.model.dto.response;

import java.util.List;

import lombok.Data;

/**
 * 订单预备信息
 * <p>
 * Created by zcd on 10/13/17.
 */
@Data
public class OrderPreInfoDTO {
    /**
     * 红包个数
     */
    Integer bonus;

    List<PrepayOptionDTO> options;

}
