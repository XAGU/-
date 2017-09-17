package com.xiaolian.amigo.data.network.model.dto.request;

import lombok.Data;

/**
 * 获取建筑列表请求DTO
 * @author zcd
 */
@Data
public class QueryResidenceListReqDTO {
    Integer buildingType;
    Integer page;
    Integer parentId;
    Integer residenceLevel;
    Integer schoolId;
    Integer size;
}
