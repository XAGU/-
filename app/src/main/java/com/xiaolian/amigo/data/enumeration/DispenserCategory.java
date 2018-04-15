package com.xiaolian.amigo.data.enumeration;

import android.support.v4.util.ObjectsCompat;

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

    public static DispenserCategory getCategoryByType(Integer type) {
        if (type == null) {
            return NORMAL;
        }
        for (DispenserCategory category : DispenserCategory.values()) {
            if (ObjectsCompat.equals(type, category.getType())) {
                return category;
            }
        }
        return NORMAL;
    }

    public int getType() {
        return type;
    }
}
