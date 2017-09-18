package com.xiaolian.amigo.data.network.model.dto.response;

import com.xiaolian.amigo.data.network.model.lostandfound.LostAndFound;

import java.util.List;

import lombok.Data;

/**
 * 失物招领列表
 * <p>
 * Created by zcd on 9/18/17.
 */
@Data
public class QueryLostAndFoundListRespDTO {
    private int total;
    private List<LostAndFound> lostAndFounds;
}
