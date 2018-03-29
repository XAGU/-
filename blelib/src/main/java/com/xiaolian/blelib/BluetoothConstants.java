package com.xiaolian.blelib;

/**
 * @author zcd
 * @date 18/3/27
 */

public class BluetoothConstants {
    /**
     * 扫描模式:经典模式
     */
    public static final int SCAN_TYPE_CLASSIC = 1;
    /**
     * 扫描模式:ble模式
     */
    public static final int SCAN_TYPE_BLE = 2;

    public static final int CONNECT_IDLE = 0x0010;
    public static final int CONNECT_CONNECTING = 0x0011;
    public static final int CONNECT_FAILURE = 0x0012;
    public static final int CONNECT_CONNECTED = 0x0013;
    public static final int CONNECT_DISCONNECT = 0x0014;
}
