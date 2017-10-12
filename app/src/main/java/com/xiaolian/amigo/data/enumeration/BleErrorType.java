package com.xiaolian.amigo.data.enumeration;

/**
 * Created by caidong on 2017/10/12.
 */
public enum BleErrorType {

    BLE_DEVICE_INVALID(20001, "蓝牙设备不合法"),
    BLE_CHECK_SUM_ERROR(20002, "校验码错误"),
    BLE_DEVICE_BUSY(20003, "设备已被占用"),
    BLE_UNKNOWN_ERROR(20004, "未知错误"),
    BLE_CMD_RESULT_ERROR(20005, "指令返回结果不合法"),
    BLE_REMAIN_AMOUNT_ERROR(20006, "剩余金额错误"),
    BLE_DEVICE_USE_NUM_ERROR(20007, "设备使用次数不对"),
    BLE_DEVICE_STATUS_ERROR(20008, "设备状态错误"),
    BLE_DEVICE_COMPUTE_ERROR(20009, "设备计算错误"),
    BLE_ORDER_STATUS_ERROR(20009, "订单状态错误");

    private int code;

    private String debugMessage;

    BleErrorType(int code, String debugMessage) {
        this.code = code;
        this.debugMessage = debugMessage;
    }

    public int getCode() {
        return code;
    }

    public String getDebugMessage() {
        return debugMessage;
    }
}
