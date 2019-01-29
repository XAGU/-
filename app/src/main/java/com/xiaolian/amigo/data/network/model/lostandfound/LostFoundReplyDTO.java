package com.xiaolian.amigo.data.network.model.lostandfound;

import lombok.Data;

/**
 * @author zcd
 * @date 18/5/14
 */
@Data
public class LostFoundReplyDTO {
    private String content;
    private Long createTime;
    private Long id;
    private String pictureUrl;
    private Long replyToUserId;
    private String replyToUserNickname;
    private Long userId;
    private String userNickname;

    /**
     * 马甲 1 普通学生 2 管理员已学生身份回复 3 管理员
     */
    private Integer vest ;

    private Integer replyVest ;


    public String getContent() {
        if (content == null) {
            content = "";
        }
        return content.replaceAll("\n+", "\n");
    }
}
