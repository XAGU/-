package com.xiaolian.amigo.data.enumeration;

import android.text.TextUtils;

import com.xiaolian.amigo.R;

/**
 * 饮水机水温
 *
 * @author zcd
 * @date 17/10/16
 */

public enum DispenserWater {
    HOT("1", "热水") {
        @Override
        public int getBackgroundDrawable() {
            return R.drawable.bg_dispenser_hot;
        }
    },
    COLD("2", "冷水") {
        @Override
        public int getBackgroundDrawable() {
            return R.drawable.bg_dispenser_cold;
        }
    },
    ICE("3", "冰水") {
        @Override
        public int getBackgroundDrawable() {
            return R.drawable.bg_dispenser_ice;
        }
    };

    String type;
    String desc;

    DispenserWater(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public abstract int getBackgroundDrawable();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static DispenserWater getTemperature(String type) {
        for (DispenserWater water : DispenserWater.values()) {
            if (TextUtils.equals(water.getType(), type)) {
                return water;
            }
        }
        return COLD;
    }
}
