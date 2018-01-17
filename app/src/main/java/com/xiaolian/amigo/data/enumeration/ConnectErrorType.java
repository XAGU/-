package com.xiaolian.amigo.data.enumeration;

/**
 * 连接错误
 * <p>
 * Created by zcd on 18/1/17.
 */

public enum ConnectErrorType {
    BLE_CONNECT_ERROR(1, "蓝牙物理连接层面失败"),
    RESULT_INVALID(2, "指令结果不合法"),
    SERVER_ERROR(3, "服务器内部错误");
    int type;
    String desc;

    ConnectErrorType(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }
}
