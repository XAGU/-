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

public class FavorDeviceDTO implements Mapper<ScanDeviceGroup> {
    private Boolean favor;
    private String location;
    private Long residenceId;
    private Integer type;
    private Integer category;
    private List<DeviceInListDTO> devices;


    public Boolean getFavor() {
        return favor;
    }

    public void setFavor(Boolean favor) {
        this.favor = favor;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getResidenceId() {
        return residenceId;
    }

    public void setResidenceId(Long residenceId) {
        this.residenceId = residenceId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public List<DeviceInListDTO> getDevices() {
        return devices;
    }

    public void setDevices(List<DeviceInListDTO> devices) {
        this.devices = devices;
    }

    @Override
    public ScanDeviceGroup transform() {
        ScanDeviceGroup scanDeviceGroup = new ScanDeviceGroup();
        scanDeviceGroup.setResidenceId(residenceId);
        scanDeviceGroup.setLocation(location);
        scanDeviceGroup.setFavor(true);
        scanDeviceGroup.setType(type);
        scanDeviceGroup.setCategory(category);
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
