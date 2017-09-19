package com.xiaolian.amigo.data.network.model.dto.response;

import com.xiaolian.amigo.data.network.model.user.School;

import java.util.List;

import lombok.Data;

/**
 * 学校列表DTO
 * @author zcd
 */
@Data
public class QueryBriefSchoolListRespDTO {
    private int total;
    private List<School> schools;
}
