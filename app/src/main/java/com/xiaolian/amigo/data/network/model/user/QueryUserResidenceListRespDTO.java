package com.xiaolian.amigo.data.network.model.user;

import com.xiaolian.amigo.data.network.model.user.UserResidence;

import java.util.List;

import lombok.Data;

/**
 * 寝室列表
 * <p>
 * Created by zcd on 9/19/17.
 */
@Data
public class QueryUserResidenceListRespDTO {
    private Integer total;
    private List<UserResidenceInListDTO> userResidences;
}
