package com.xiaolian.amigo.data.network.model.user;

import lombok.Data;

/**
 * 上传用户设备信息
 * <p>
 * Created by zcd on 17/12/12.
 */
@Data
public class UploadUserDeviceInfoReqDTO {
    public static final int SYSTEM_CODE = 2;
    private String appVersion;
    private String brand;
    private String model;
    private Integer system;
    private String systemVersion;
    private String uniqueId;
}
