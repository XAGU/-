package com.xiaolian.amigo.ui.wallet;

import lombok.Data;

/**
 * wallet模块事件
 *
 * @author zcd
 * @date 17/11/27
 */
@Data
public class WalletEvent {
    private EventType eventType;
    private Object object;

    public WalletEvent(EventType eventType, Object object) {
        this.eventType = eventType;
        this.object = object;
    }

    public enum EventType {
        /**
         * 删除账户
         */
        DELETE_ACCOUNT;
    }
}
