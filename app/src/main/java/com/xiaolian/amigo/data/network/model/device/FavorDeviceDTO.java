package com.xiaolian.amigo.data.network.model.device;

import com.xiaolian.amigo.data.vo.Mapper;
import com.xiaolian.amigo.data.vo.ScanDevice;
import com.xiaolian.amigo.data.vo.ScanDeviceGroup;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * <p>
 * Created by zcd on 18/1/3.
 */
@Data
public class FavorDeviceDTO implements Mapper<ScanDeviceGroup> {
    private Boolean favor;
    private String location;
    private Long residenceId;
    private Integer type;
    private List<DeviceInListDTO> devices;

    @Override
    public ScanDeviceGroup transform() {
        ScanDeviceGroup scanDeviceGroup = new ScanDeviceGroup();
        scanDeviceGroup.setResidenceId(residenceId);
        scanDeviceGroup.setLocation(location);
        scanDeviceGroup.setFavor(true);
        scanDeviceGroup.setType(type);
        List<ScanDevice> water = new ArrayList<>();
        if (!devices.isEmpty()) {
            for (DeviceInListDTO deviceInListDTO : devices) {
                water.add(deviceInListDTO.transform());
            }
        }
        scanDeviceGroup.setWater(water);
        return scanDeviceGroup;
    }
}
