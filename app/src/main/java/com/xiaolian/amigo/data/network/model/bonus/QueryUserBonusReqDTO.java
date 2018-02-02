package com.xiaolian.amigo.data.network.model.bonus;

import lombok.Data;

/**
 * 代金券接口请求DTO
 *
 * @author zcd
 * @date 17/9/18
 */
@Data
public class QueryUserBonusReqDTO {
    private Integer page;
    private Integer size;
    private Integer useStatus;
    private Integer validStatus;
    private Integer deviceType;
}
