package com.xiaolian.amigo.data.network.model.dto.response;

import com.xiaolian.amigo.data.network.model.user.BriefSchoolBusiness;

import java.util.List;

import lombok.Data;

/**
 * 学校业务列表DTO
 * @author zcd
 */
@Data
public class QuerySchoolBizListRespDTO {
    private List<BriefSchoolBusiness> businesses;
}
