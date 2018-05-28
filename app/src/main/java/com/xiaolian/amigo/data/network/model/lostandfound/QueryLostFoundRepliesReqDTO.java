package com.xiaolian.amigo.data.network.model.lostandfound;

import lombok.Data;

/**
 * @author zcd
 * @date 18/5/16
 */
@Data
public class QueryLostFoundRepliesReqDTO {
    private Integer from;
    private Long id;
    private Integer size;
    private Integer status = 1;
}
