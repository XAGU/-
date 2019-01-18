package com.xiaolian.amigo.data.network.model.lostandfound;

import lombok.Data;

/**
 * @author zcd
 * @date 18/6/22
 */
@Data
public class LostFoundNoticeDTO {
    /**
     * 关联对象的内容，失物招领的标题、评论/回复的内容
     */
    private String content;
    private Long createTime;
    private Long createUserId;
    private Long id;
    /**
     * 关联对象的id，当类型为回复时
     */
    private Long itemId;
    /**
     * 关联对象的类型 1 失物招领 2 评论 3 回复
     */
    private Integer itemType;
    /**
     * 所属失物招领的id
     */
    private Long lostFoundId;
    /**
     * 发起通知用户头像地址
     */
    private String pictureUrl;
    /**
     * 通知类型 1 回复 2 点赞
     */
    private Integer type;
    /**
     * 被通知用户id
     */
    private Long userId;
    /**
     * 发起通知用户昵称
     */
    private String userNickname;
    /**
     * 失物招领类型（1-失主 2-拾主）
     */
    private Integer lostFoundType;


    /**
     * 马甲 1 普通学生 2 管理员已学生身份回复 3 管理员
     */
    private Integer vest ;
}
