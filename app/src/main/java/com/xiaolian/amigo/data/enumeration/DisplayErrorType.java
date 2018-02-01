package com.xiaolian.amigo.data.enumeration;

/**
 * 连接显示错误
 * <p>
 * Created by zcd on 18/1/17.
 */

public enum  DisplayErrorType {
    CONNECT_ERROR(1, "页面显示连接失败"),
    DEVICE_ERROR(2, "设备故障"),
    SYSTEM_ERROR(3, "系统错误");
    int type;
    String desc;

    DisplayErrorType(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }
}
