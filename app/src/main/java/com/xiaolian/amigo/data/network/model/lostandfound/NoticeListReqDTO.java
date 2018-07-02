package com.xiaolian.amigo.data.network.model.lostandfound;

import lombok.Data;

/**
 * @author zcd
 * @date 18/6/22
 */
@Data
public class NoticeListReqDTO {
    private Integer page;
    private Integer size;
    /**
     * 通知内容类型 1 回复 2 点赞
     */
    private Integer type;
}
