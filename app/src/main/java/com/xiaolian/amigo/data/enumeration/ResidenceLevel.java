package com.xiaolian.amigo.data.enumeration;

/**
 * 住所类别
 * <p>
 * Created by zcd on 10/19/17.
 */

public enum  ResidenceLevel {
    UNKNOWN(-1, "未知"),
    BUILDING(1, "楼栋"),
    FLOOR(2, "楼层"),
    ROOM(3, "房间")
    ;
    private int type;
    private String desc;

    ResidenceLevel(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public static ResidenceLevel getResidenceLevel(int type) {
        for (ResidenceLevel level : ResidenceLevel.values()) {
            if (level.getType() == type) {
                return level;
            }
        }
        return UNKNOWN;
    }
}
