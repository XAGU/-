package com.xiaolian.blelib;

import java.util.UUID;

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

    /** The profile is in disconnected state */
    public static final int STATE_DISCONNECTED  = 0;
    /** The profile is in connecting state */
    public static final int STATE_CONNECTING    = 1;
    /** The profile is in connected state */
    public static final int STATE_CONNECTED     = 2;
    /** The profile is in disconnecting state */
    public static final int STATE_DISCONNECTING = 3;

    public static final int CONN_RESPONSE_SUCCESS = 0x011;
    public static final int CONN_RESPONSE_FAIL = 0x012;

    /** A GATT operation completed successfully */
    public static final int GATT_SUCCESS = 0;
    /** GATT read operation is not permitted */
    public static final int GATT_READ_NOT_PERMITTED = 0x2;
    /** GATT write operation is not permitted */
    public static final int GATT_WRITE_NOT_PERMITTED = 0x3;
    /** Insufficient authentication for a given operation */
    public static final int GATT_INSUFFICIENT_AUTHENTICATION = 0x5;
    /** The given request is not supported */
    public static final int GATT_REQUEST_NOT_SUPPORTED = 0x6;
    /** Insufficient encryption for a given operation */
    public static final int GATT_INSUFFICIENT_ENCRYPTION = 0xf;
    /** A read or write operation was requested with an invalid offset */
    public static final int GATT_INVALID_OFFSET = 0x7;
    /** A write operation exceeds the maximum length of the attribute */
    public static final int GATT_INVALID_ATTRIBUTE_LENGTH = 0xd;
    /** A remote device connection is congested. */
    public static final int GATT_CONNECTION_CONGESTED = 0x8f;
    /** A GATT operation failed, errors other than the above */
    public static final int GATT_FAILURE = 0x101;
    /**
     * 其他错误
     */
    public static final int GATT_OTHER_FAILURE = 0x111;

    public static final UUID CLIENT_CHARACTERISTIC_CONFIG = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
}
