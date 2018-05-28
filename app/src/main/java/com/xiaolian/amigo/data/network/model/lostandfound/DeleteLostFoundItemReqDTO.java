package com.xiaolian.amigo.data.network.model.lostandfound;

import lombok.Data;

/**
 * @author zcd
 * @date 18/5/16
 */
@Data
public class DeleteLostFoundItemReqDTO {
    private Long id;
    /**
     * 1 失物 2 招领 3 评论 4 回复
     */
    private Integer type;
}
