package com.xiaolian.amigo.data.base;

/**
 * time holder
 *
 * @author caidong
 * @date 17/10/15
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

    public static void main(String[] args) {
        String currentUrl = "http://123:5081/abc";
        String oldUserServer = "http://123:5081";
        String oldBathServer = "http://123:5082";
        String newUserServer = "http://321:5081";
        String newBathServer = "http://321:5082";
        String replacedUrl = currentUrl.replace(oldBathServer, newBathServer);
        replacedUrl = replacedUrl.replace(oldUserServer, newUserServer);
        System.out.println(replacedUrl);

    }
}
