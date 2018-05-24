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

    public String getContent() {
        if (content == null) {
            content = "";
        }
        return content.replaceAll("\n+", "\n");
    }
}
