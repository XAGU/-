package com.xiaolian.amigo.data.network.model.dto.response;

import com.xiaolian.amigo.data.network.model.bonus.Bonus;

import java.util.List;

import lombok.Data;

/**
 * 红包列表DTO
 * @author zcd
 */
@Data
public class QueryUserBonusListRespDTO {

    private int total;
    private List<Bonus> bonuses;
}
