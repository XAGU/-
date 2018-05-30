package com.xiaolian.amigo.data.network.model.lostandfound;

import lombok.Data;

/**
 * @author zcd
 * @date 18/5/15
 */
@Data
public class SaveLostFoundCommentsRepliesDTO {
    private String content;
    private Long lostFoundId;
    private Long replyToId;
    private Long replyToUserId;
    private Integer type;
}
