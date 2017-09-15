package com.xiaolian.amigo.data.network.model.dto.request;

import lombok.Data;

/**
 * 获取建筑列表请求DTO
 * @author zcd
 */
@Data
public class QueryResidenceListReqDTO {
    int buildingType;
    int page;
    int parentId;
    int residenceLevel;
    int schoolId;
    int size;
}
