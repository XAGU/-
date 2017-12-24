package com.xiaolian.amigo.data.network.model.bonus;

import com.xiaolian.amigo.data.network.model.bonus.UserBonusDTO;
import com.xiaolian.amigo.data.vo.Bonus;

import java.util.List;

import lombok.Data;

/**
 * 代金券列表DTO
 * @author zcd
 */
@Data
public class QueryUserBonusListRespDTO {

    private Integer total;
    private List<UserBonusDTO> bonuses;
}
