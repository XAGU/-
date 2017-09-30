package com.xiaolian.amigo.data.network.model.dto.request;

import lombok.Data;

/**
 * 查询设备列表reqDTO
 * <p>
 * Created by zcd on 9/30/17.
 */
@Data
public class QueryDeviceListReqDTO {
    private Integer healthStatus;
    private Integer page;
    private Long residenceId;
    private Long schoolId;
    private String selectKey;
    private Integer size;
    private Long supplierId;
    private Integer type;
}
