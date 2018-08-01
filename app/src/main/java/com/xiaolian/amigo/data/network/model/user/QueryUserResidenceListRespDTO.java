package com.xiaolian.amigo.data.network.model.user;

import java.util.List;

import lombok.Data;

/**
 * 用户记录的洗澡地址列表
 * @author zcd
 * @date 17/9/19
 */
@Data
public class QueryUserResidenceListRespDTO {
    private Integer total;
    private List<UserResidenceInListDTO> userResidences;  //  洗澡地址信息
}
