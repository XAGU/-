package com.xiaolian.amigo.data.network.model.dto.response;

import com.xiaolian.amigo.data.network.model.bonus.Bonus;

import java.util.List;

import lombok.Data;

/**
 * 代金券列表DTO
 * @author zcd
 */
@Data
public class QueryUserBonusListRespDTO {

    private Integer total;
    private List<Bonus> bonuses;
}
