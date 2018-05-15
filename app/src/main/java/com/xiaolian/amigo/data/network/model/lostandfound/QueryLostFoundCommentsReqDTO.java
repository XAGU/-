package com.xiaolian.amigo.data.network.model.lostandfound;

import lombok.Data;

/**
 * @author zcd
 * @date 18/5/14
 */
@Data
public class QueryLostFoundCommentsReqDTO {
    private Integer commentsSize;
    private Integer from;
    private Long id;
    private Integer repliesSize;
}
