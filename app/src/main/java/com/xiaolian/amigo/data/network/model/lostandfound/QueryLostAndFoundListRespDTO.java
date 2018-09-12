package com.xiaolian.amigo.data.network.model.lostandfound;

import java.util.List;

import lombok.Data;

/**
 * 失物招领列表
 *
 * @author zcd
 * @date 17/9/18
 */
@Data
public class QueryLostAndFoundListRespDTO {



    private Integer total;
    private List<LostAndFoundDTO> posts;
    private List<LostAndFoundDTO> hotPosts ;
    private int size ;
    private int totalHidden ;
    private int totalNormal ;

    /**
     * 是否开启评论
     */
    private Boolean commentEnable;
}
