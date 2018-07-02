package com.xiaolian.amigo.data.network.model.lostandfound;

import lombok.Data;

/**
 * @author zcd
 * @date 18/5/14
 */
@Data
public class QueryLostFoundCommentsReqDTO {
    private Integer commentsSize;
    private String excludeId;
    private Integer from;
    private Integer hot;
    private Long id;
    private Integer repliesSize;
    private Integer status;
}
