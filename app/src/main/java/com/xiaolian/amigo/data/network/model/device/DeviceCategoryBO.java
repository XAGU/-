package com.xiaolian.amigo.data.network.model.device;

import com.xiaolian.amigo.data.vo.DeviceCategory;
import com.xiaolian.amigo.data.vo.Mapper;

import java.util.List;

import lombok.Data;

/**
 * 设备供应商
 *
 * @author zcd
 * @date 17/12/18
 */
@Data
public class DeviceCategoryBO implements Mapper<DeviceCategory> {
    private Integer deviceType;
    private List<Supplier> suppliers;

    @Override
    public DeviceCategory transform() {
        DeviceCategory deviceCategory = new DeviceCategory();
        deviceCategory.setDeviceType(deviceType);
        deviceCategory.setSuppliers(suppliers);
        return deviceCategory;
    }
}
