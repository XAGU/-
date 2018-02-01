package com.xiaolian.amigo.data.network.model.common;

import lombok.Data;

/**
 * SimpleQueryReqDTO
 *
 * @author zcd
 * @date 17/9/15
 */
@Data
public class SimpleQueryReqDTO {
    private Integer page;
    private Integer size;
    private Integer schoolId;
}
