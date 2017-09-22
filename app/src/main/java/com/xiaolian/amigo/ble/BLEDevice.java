package com.xiaolian.amigo.ble;

import lombok.Data;

/**
 * 蓝牙设备信息
 * <p>
 * Created by caidong on 2017/9/22.
 */
@Data
public class BLEDevice {
    // 设备名称
    private String name;
    // 蓝牙mac地址
    private String mac;
    // 设备信号衰减值
    private int rssi;
}
