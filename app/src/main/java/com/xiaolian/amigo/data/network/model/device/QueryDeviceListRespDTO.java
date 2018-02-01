package com.xiaolian.amigo.data.network.model.device;

import java.util.List;

import lombok.Data;

/**
 * @author zcd
 * @date 18/1/3
 */
@Data
public class QueryDeviceListRespDTO {
    private List<DeviceDTO> devices;
    private Integer total;
}
