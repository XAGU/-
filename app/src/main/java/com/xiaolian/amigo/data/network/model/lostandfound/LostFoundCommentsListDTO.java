package com.xiaolian.amigo.data.network.model.lostandfound;

import java.util.List;

import lombok.Data;

/**
 * @author zcd
 * @date 18/5/14
 */
@Data
public class LostFoundCommentsListDTO {
    private List<LostFoundCommentDTO> comments;
    private Integer commentsSize;
}
