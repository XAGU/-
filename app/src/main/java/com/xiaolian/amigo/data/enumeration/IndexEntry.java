package com.xiaolian.amigo.data.enumeration;

/**
 * @author zcd
 * @date 18/3/19
 */

public enum IndexEntry {
    SHOWER(1, "热水澡"),
    WATER(2, "饮水机"),
    DRYER(3, "吹风机"),
    WASHER(4, "洗衣机"),
    GATE(101, "门禁卡"),;
    private Integer code;
    private String desc;

    IndexEntry(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
