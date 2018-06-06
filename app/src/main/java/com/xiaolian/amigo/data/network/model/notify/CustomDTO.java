package com.xiaolian.amigo.data.network.model.notify;

import lombok.Data;

/**
 * @author zcd
 * @date 18/6/5
 */
@Data
public class CustomDTO {
    /**
     * 1: nothing
     * 2: repairDetail
     * 3: taskDetail
     */
    private Integer action;
    private Long targetId;
    private String targetUri;
}
