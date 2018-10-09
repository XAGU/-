package com.xiaolian.amigo.data.network.model.lostandfound;

import java.util.List;

import lombok.Data;

/**
 * @author zcd
 * @date 18/5/14
 */
@Data
public class LostFoundCommentDTO {
    private String content;
    private Long createTime;
    private Long id;
    private String pictureUrl;
    private List<LostFoundReplyDTO> replies;
    private Integer repliesCount;
    private Integer repliesDelCount;
    private Long userId;
    private String userNickname;
    /**
     * 点赞数量
     */
    private Integer likeCount;


    private String topicName;
    /**
     * 本人是否点赞 1 本人已点赞 2 本人未点赞
     */
    private Integer liked;

    public String getContent() {
        if (content == null) {
            content = "";
        }
        return content.replaceAll("\n+", "\n");
    }
}
