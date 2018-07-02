package com.xiaolian.amigo.data.network.model.lostandfound;

import lombok.Data;

/**
 * @author zcd
 * @date 18/6/21
 */
@Data
public class CollectListReqDTO {
    private Integer page;
    private Integer size;
    private Integer type;
}
