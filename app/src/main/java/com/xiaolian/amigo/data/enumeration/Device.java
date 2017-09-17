package com.xiaolian.amigo.data.enumeration;

/**
 * 设备类型
 * <p>
 * Created by caidong on 2017/9/15.
 */

public enum Device {
    HEARTER(1), DISPENSER(2);

    private int type;

    Device(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static Device getDevice(int type) {
        for (Device device : Device.values()) {
            if (device.getType() == type) {
                return device;
            }
        }
        return null;
    }
}
