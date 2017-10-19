package com.xiaolian.amigo.data.enumeration;

/**
 * 通知未读已读状态
 * <p>
 * Created by zcd on 10/19/17.
 */

public enum NoticeReadStatus {
    UNREAD(1, "未读"),
    READ(2, "已读");
    private int type;
    private String desc;

    NoticeReadStatus(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public static NoticeReadStatus getReadStatus(int type) {
        for (NoticeReadStatus status : NoticeReadStatus.values()) {
            if (status.getType() == type) {
                return status;
            }
        }
        return UNREAD;
    }
}
