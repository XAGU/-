package com.xiaolian.amigo.data.network.model.device;

import com.xiaolian.amigo.data.vo.Mapper;
import com.xiaolian.amigo.data.vo.ScanDevice;
import com.xiaolian.amigo.data.vo.ScanDeviceGroup;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * @author zcd
 * @date 18/1/3
 */
@Data
public class DeviceDTO implements Mapper<ScanDeviceGroup> {
    private List<DeviceInListDTO> devices;
    private Boolean favor;
    private String location;
    private Long residenceId;
    private Integer type;

    @Override
    public ScanDeviceGroup transform() {
        ScanDeviceGroup scanDeviceGroup = new ScanDeviceGroup();
        scanDeviceGroup.setResidenceId(residenceId);
        scanDeviceGroup.setLocation(location);
        scanDeviceGroup.setFavor(favor);
        scanDeviceGroup.setType(type);
        List<ScanDevice> waters = new ArrayList<>();
        if (!devices.isEmpty()) {
            for (DeviceInListDTO deviceInListDTO : devices) {
                waters.add(deviceInListDTO.transform());
            }
        }
        scanDeviceGroup.setWater(waters);
        return scanDeviceGroup;
    }
}
