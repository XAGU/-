package com.xiaolian.amigo.data.enumeration;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.device.dispenser.DispenserActivity;
import com.xiaolian.amigo.ui.device.heater.HeaterActivity;

/**
 * 设备类型
 * <p>
 * Created by caidong on 2017/9/15.
 */

public enum Device {
    UNKNOWN(-1, "未知设备") {
        @Override
        public int getColorRes() {
            return 0;
        }

        @Override
        public int getDrawableRes() {
            return 0;
        }

        @Override
        public Class getClz() {
            return null;
        }
    },
    HEATER(1, "热水澡") {
        @Override
        public int getColorRes() {
            return R.color.device_heator;
        }

        @Override
        public int getDrawableRes() {
            return R.drawable.shower;
        }

        @Override
        public Class getClz() {
            return HeaterActivity.class;
        }
    }, DISPENSER(2, "饮水机") {
        @Override
        public int getColorRes() {
            return R.color.device_dispenser;
        }

        @Override
        public int getDrawableRes() {
            return R.drawable.water;
        }

        @Override
        public Class getClz() {
            return DispenserActivity.class;
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

    public abstract int getDrawableRes();

    public abstract Class getClz();

    public static Device getDevice(int type) {
        for (Device device : Device.values()) {
            if (device.getType() == type) {
                return device;
            }
        }
        return UNKNOWN;
    }
}
