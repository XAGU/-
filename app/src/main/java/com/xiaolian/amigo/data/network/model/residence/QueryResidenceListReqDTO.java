package com.xiaolian.amigo.data.network.model.residence;

import lombok.Data;

/**
 * 获取建筑列表请求DTO
 * residenceLevel 1幢 2楼层 3宿舍 具体位置 buildingType 1宿舍楼 parentId上一层事物Id
 *
 * @author zcd
 */
@Data
public class QueryResidenceListReqDTO {
    Integer buildingType;
    Integer deviceType;
    Integer page;
    Integer size;
    Long parentId;
    Integer residenceLevel;
    Long schoolId;
    Boolean existDevice;
}
