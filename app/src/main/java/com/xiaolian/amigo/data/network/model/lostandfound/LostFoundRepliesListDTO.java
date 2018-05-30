package com.xiaolian.amigo.data.network.model.lostandfound;

import java.util.List;

import lombok.Data;

/**
 * @author zcd
 * @date 18/5/16
 */
@Data
public class LostFoundRepliesListDTO {
    private List<LostFoundReplyDTO> replies;
    private Integer size;
}
