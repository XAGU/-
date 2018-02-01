package com.xiaolian.amigo.data.network.model.device;

import com.xiaolian.amigo.data.vo.Mapper;
import com.xiaolian.amigo.data.vo.ScanDevice;
import com.xiaolian.amigo.data.vo.ScanDeviceGroup;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * 饮水机
 *
 * @author zcd
 * @date 17/12/14
 */
@Data
public class WaterInListDTO implements Mapper<ScanDeviceGroup> {
    /**
     * 是否已收藏
     */
    private Boolean favor;

    private String location;

    private Long residenceId;

    private List<DeviceInListDTO> water;

    @Override
    public ScanDeviceGroup transform() {
        ScanDeviceGroup scanDeviceGroup = new ScanDeviceGroup();
        scanDeviceGroup.setFavor(favor);
        scanDeviceGroup.setLocation(location);
        scanDeviceGroup.setResidenceId(residenceId);
        List<ScanDevice> devices = new ArrayList<>();
        if (water != null && !water.isEmpty()) {
            for (DeviceInListDTO deviceInListDTO : water) {
                devices.add(deviceInListDTO.transform());
            }
        }
        scanDeviceGroup.setWater(devices);
        return scanDeviceGroup;
    }
}
