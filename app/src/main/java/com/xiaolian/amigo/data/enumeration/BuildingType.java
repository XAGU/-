package com.xiaolian.amigo.data.enumeration;

/**
 * 建筑类别
 *
 * @author zcd
 * @date 17/10/19
 */

public enum BuildingType {
    DORMITORY(1, "宿舍"),
    OTHER(2, "其他");
    private int type;
    private String desc;

    BuildingType(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public static BuildingType getBuildingType(int type) {
        for (BuildingType buildingType : BuildingType.values()) {
            if (buildingType.getType() == type) {
                return buildingType;
            }
        }
        return OTHER;
    }
}
