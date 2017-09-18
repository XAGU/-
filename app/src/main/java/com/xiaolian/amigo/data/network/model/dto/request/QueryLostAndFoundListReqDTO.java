package com.xiaolian.amigo.data.network.model.dto.request;

import lombok.Data;

/**
 * 失物招领列表请求DTO
 * <p>
 * Created by zcd on 9/18/17.
 */
@Data
public class QueryLostAndFoundListReqDTO {
    private Integer page;
    private Integer schoolId;
    private String selectKey;
    private Integer size;
    private Integer type;
}
