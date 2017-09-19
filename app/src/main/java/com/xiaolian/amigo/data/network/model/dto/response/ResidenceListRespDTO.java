package com.xiaolian.amigo.data.network.model.dto.response;

import com.xiaolian.amigo.data.network.model.user.Residence;

import java.util.List;

import lombok.Data;

/**
 * ResidenceListRespDTO
 * <p>
 * Created by zcd on 9/19/17.
 */
@Data
public class ResidenceListRespDTO {
    private int total;
    private List<Residence> residences;
}
