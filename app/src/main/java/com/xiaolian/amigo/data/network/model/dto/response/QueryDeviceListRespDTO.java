package com.xiaolian.amigo.data.network.model.dto.response;

import com.xiaolian.amigo.data.network.model.device.Device;

import java.util.List;

import lombok.Data;

/**
 * 查询设备列表respDTO
 * <p>
 * Created by zcd on 9/30/17.
 */
@Data
public class QueryDeviceListRespDTO {
    private List<Device> devices;
    private Integer total;
}
