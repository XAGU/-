package com.xiaolian.amigo.data.network.model.user;

import java.util.List;

import lombok.Data;

/**
 * 寝室列表
 *
 * @author zcd
 * @date 17/9/19
 */
@Data
public class QueryUserResidenceListRespDTO {
    private Integer total;
    private List<UserResidenceInListDTO> userResidences;
}
