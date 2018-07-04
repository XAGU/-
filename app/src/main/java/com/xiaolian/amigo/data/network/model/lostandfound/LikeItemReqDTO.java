package com.xiaolian.amigo.data.network.model.lostandfound;

import lombok.Data;

/**
 * @author zcd
 * @date 18/6/22
 */
@Data
public class LikeItemReqDTO {
    /**
     * 被点赞/取消点赞的对象id，失物招领或者评论
     */
    private Long itemId;
    /**
     * 是否是点赞，1 点赞 2 取消点赞
     */
    private Integer like;
    /**
     * 被点赞/取消点赞的类型，1 失物招领 2 评论
     */
    private Integer type;
}
