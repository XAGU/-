package com.xiaolian.amigo.data.enumeration;

/**
 * <p>
 * Created by zcd on 10/17/17.
 */

public enum LostAndFound {
    LOST(1, "失物"),
    FOUND(2, "招领")
    ;

    LostAndFound(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    private int type;
    private String desc;

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

    public static LostAndFound getLostAndFound(int type) {
        for (LostAndFound lostAndFound : LostAndFound.values()) {
            if (lostAndFound.getType() == type) {
                return lostAndFound;
            }
        }
        return LOST;
    }
}
