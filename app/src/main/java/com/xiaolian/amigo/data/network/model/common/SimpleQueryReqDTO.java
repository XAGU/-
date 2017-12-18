package com.xiaolian.amigo.data.network.model.common;

import lombok.Data;

/**
 * SimpleQueryReqDTO
 * @author zcd
 */
@Data
public class SimpleQueryReqDTO {
    private Integer page;
    private Integer size;
    private Integer schoolId;
}
