package com.xiaolian.amigo.data.network.model.dto.request;

import lombok.Data;

/**
 * 红包接口请求DTO
 * @author zcd
 */
@Data
public class QueryUserBonusReqDTO {
    private Integer page;
    private Integer size;
    private Integer useStatus;
    private Integer validStatus;
    private Integer deviceType;
}
