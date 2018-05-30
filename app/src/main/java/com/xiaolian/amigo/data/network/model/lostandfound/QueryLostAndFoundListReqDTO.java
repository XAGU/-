package com.xiaolian.amigo.data.network.model.lostandfound;

import lombok.Data;

/**
 * 失物招领列表请求DTO
 *
 * @author zcd
 * @date 17/9/18
 */
@Data
public class QueryLostAndFoundListReqDTO {
    private Integer page;
    private Integer size;
    private Long schoolId;
    private String selectKey;
    /**
     * 1 表示失物 2 表示招领
     */
    private Integer type;
    /**
     * 必传1
     */
    private Integer status = 1;
}
