package com.xiaolian.amigo.data.network.model.dto.request;

import lombok.Data;

/**
 * 提醒客服DTO
 * <p>
 * Created by zcd on 10/20/17.
 */
@Data
public class RemindReqDTO {
    // type:1、提现 2、维修，sourceId为提现申请的id或者维修的id
    private Integer type;
    private Long sourceId;
}
