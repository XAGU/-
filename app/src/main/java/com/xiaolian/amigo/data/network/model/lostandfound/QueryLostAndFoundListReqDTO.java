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
    private Long topicId;
    private String selectKey;
    private String hotPostIds;
}
