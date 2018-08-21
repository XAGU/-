package com.xiaolian.amigo.data.network.model.device;

import lombok.Data;

/**
 * 设备详情
 */
@Data
public class BriefDeviceDTO {

    /**
     * deviceType : 0
     * id : 0
     * location : string
     * schoolId : 0
     * schoolName : string
     * supplierId : 0
     */

    private int deviceType;
    private long id;
    private String location;
    private long schoolId;
    private String schoolName;
    private long supplierId; // 设备供应商
}
