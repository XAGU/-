package com.xiaolian.amigo.data.network.model.connecterror;

import lombok.Data;

/**
 * 上报设备连接错误
 * <p>
 * Created by zcd on 18/1/17.
 */
@Data
public class DeviceConnectErrorReqDTO {
    // connnectErrorType取值为1-蓝牙物理连接层面失败 2-指令结果不合法 3-服务器内不错，
    // displayErrorType取值为1-页面显示连接失败 2-设备故障 3-系统错误

    private String extra;
    private String macAddress;
    private Integer connnectErrorType;
    private Integer displayErrorType;
    private String reason;
    private Integer step;
}
