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
}
