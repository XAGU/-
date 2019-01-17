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

    public List<String> getAfterOrderCopy() {
        return afterOrderCopy;
    }

    public void setAfterOrderCopy(List<String> afterOrderCopy) {
        this.afterOrderCopy = afterOrderCopy;
    }

    public List<String> getPreOrderCopy() {
        return preOrderCopy;
    }

    public void setPreOrderCopy(List<String> preOrderCopy) {
        this.preOrderCopy = preOrderCopy;
    }

    /**
     * 1、普通饮水机 2、三合一饮水机
     **/
    private Integer category;
    private List<DeviceInListDTO> devices;
    private Boolean favor;
    private String location;
    private Long residenceId;
    private Integer type;

    /**
     * 下单前文案
     */
    private List<String> afterOrderCopy ;

    /**
     * 下单后文案
     */
    private List<String> preOrderCopy ;



    @Override
    public ScanDeviceGroup transform() {
        ScanDeviceGroup scanDeviceGroup = new ScanDeviceGroup();
        scanDeviceGroup.setCategory(category);
        scanDeviceGroup.setResidenceId(residenceId);
        scanDeviceGroup.setLocation(location);
        scanDeviceGroup.setFavor(favor);
        scanDeviceGroup.setType(type);
        scanDeviceGroup.setAfterOrderCopy(afterOrderCopy);
        scanDeviceGroup.setPreOrderCopy(preOrderCopy);
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
