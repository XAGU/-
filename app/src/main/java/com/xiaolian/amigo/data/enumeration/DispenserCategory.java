package com.xiaolian.amigo.data.enumeration;

/**
 * @author zcd
 * @date 18/4/3
 */

public enum DispenserCategory {
    /**
     * 普通饮水机
     */
    NORMAL(1),
    /**
     * 三合一饮水机
     */
    MULTI(2);
    private int type;

    DispenserCategory(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
