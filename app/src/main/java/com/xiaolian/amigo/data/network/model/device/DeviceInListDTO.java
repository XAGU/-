package com.xiaolian.amigo.data.network.model.device;

import com.xiaolian.amigo.data.vo.Mapper;
import com.xiaolian.amigo.data.vo.ScanDevice;

import lombok.Data;

/**
 * <p>
 * Created by zcd on 17/12/14.
 */
@Data
public class DeviceInListDTO implements Mapper<ScanDevice> {
    private Long id;
    private String macAddress;
    private Double price;
    private Integer pulse;
    private String usefor;
    private String name;

    private Long supplierId;

    @Override
    public ScanDevice transform() {
        ScanDevice scanDevice = new ScanDevice();
        scanDevice.setId(id);
        scanDevice.setMacAddress(macAddress);
        scanDevice.setPrice(price);
        scanDevice.setPulse(pulse);
        scanDevice.setUsefor(usefor);
        scanDevice.setName(name);
        scanDevice.setSupplierId(supplierId);
        return scanDevice;
    }
}
