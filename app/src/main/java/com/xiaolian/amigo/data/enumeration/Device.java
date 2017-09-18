package com.xiaolian.amigo.data.enumeration;

import com.xiaolian.amigo.R;

/**
 * 设备类型
 * <p>
 * Created by caidong on 2017/9/15.
 */

public enum Device {
    HEARTER(1, "热水器") {
        @Override
        public int getColorRes() {
            return R.color.device_heator;
        }
    }, DISPENSER(2, "饮水机") {
        @Override
        public int getColorRes() {
            return R.color.device_dispenser;
        }
    };

    private int type;

    private String desc;

    Device(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public abstract int getColorRes();

    public static Device getDevice(int type) {
        for (Device device : Device.values()) {
            if (device.getType() == type) {
                return device;
            }
        }
        return null;
    }
}
