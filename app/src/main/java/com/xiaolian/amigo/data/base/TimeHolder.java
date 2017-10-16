package com.xiaolian.amigo.data.base;

/**
 * Created by caidong on 2017/10/15.
 */
public class TimeHolder {

    private volatile static TimeHolder holder;
    private Long lastConnectTime;

    private TimeHolder() {

    }

    public static TimeHolder get() {
        if (null == holder) {
            synchronized (TimeHolder.class) {
                if (null == holder) {
                    holder = new TimeHolder();
                }
            }
        }
        return holder;
    }

    public Long getLastConnectTime() {
        return lastConnectTime;
    }

    public void setLastConnectTime(Long lastConnectTime) {
        this.lastConnectTime = lastConnectTime;
    }
}
