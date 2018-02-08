package com.xiaolian.amigo.data.network.model.residence;

import com.xiaolian.amigo.data.network.model.user.Residence;

import java.util.List;

import lombok.Data;

/**
 * ResidenceListRespDTO
 *
 * @author zcd
 * @date 17/9/19
 */
@Data
public class ResidenceListRespDTO {
    private Integer total;
    private List<Residence> residences;
}
