package com.xiaolian.amigo.data.enumeration;

import android.text.TextUtils;

import java.util.Objects;

/**
 * 饮水机水温
 * <p>
 * Created by zcd on 10/16/17.
 */

public enum  DispenserWater {
    HOT("1", "热水"),
    COLD("2", "冷水"),
    ICE("3", "冰水")
    ;

    String type;
    String desc;

    DispenserWater(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

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
