package com.xiaolian.amigo.data.enumeration;

/**
 * 设备类型
 * <p>
 * Created by caidong on 2017/9/15.
 */

public enum Device {
    HEARTER(1) {
        @Override
        public String getDesc() {
            return "热水器";
        }
    }, DISPENSER(2) {
        @Override
        public String getDesc() {
            return "饮水机";
        }
    };

    private int type;

    public abstract String getDesc();

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
