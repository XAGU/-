package com.xiaolian.amigo.data.network.model.bonus;

import java.util.List;

import lombok.Data;

/**
 * 代金券列表DTO
 *
 * @author zcd
 * @date 17/12/14
 */
@Data
public class QueryUserBonusListRespDTO {

    private Integer total;
    private List<UserBonusDTO> bonuses;
}
