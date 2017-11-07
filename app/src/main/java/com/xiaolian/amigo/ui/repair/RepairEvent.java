package com.xiaolian.amigo.ui.repair;

/**
 * 维修模块事件
 * <p>
 * Created by zcd on 17/11/7.
 */

public enum  RepairEvent {
    REFRESH_REPAIR_LIST(1);
    int type;

    RepairEvent(int type) {
        this.type = type;
    }
}
