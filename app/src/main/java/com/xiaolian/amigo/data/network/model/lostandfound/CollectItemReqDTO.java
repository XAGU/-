package com.xiaolian.amigo.data.network.model.lostandfound;

import lombok.Data;

/**
 * @author zcd
 * @date 18/6/21
 */
@Data
public class CollectItemReqDTO {
    /**
     * 是否是收藏 1 收藏 2 取消收藏
     */
    private Integer collect;
    /**
     * 被收藏的失物招领id
     */
    private Long lostFoundId;
}
