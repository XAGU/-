package com.xiaolian.amigo.ui.repair;

/**
 * 维修模块事件
 *
 * @author zcd
 * @date 17/11/7
 */

public enum RepairEvent {
    /**
     * 刷新报修记录列表
     */
    REFRESH_REPAIR_LIST(1);
    int type;

    RepairEvent(int type) {
        this.type = type;
    }
}
