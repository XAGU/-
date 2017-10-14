package com.xiaolian.amigo.data.enumeration;

/**
 * Created by caidong on 2017/10/13.
 */
public enum ErrorTag {
    CONNECT_ERROR(1), DEVICE_BUSY(2), REPAIR(3), CALL(4);

    ErrorTag(int code) {
        this.code = code;
    }

    public static ErrorTag getErrorTag(int code) {
        for (ErrorTag tag : ErrorTag.values()) {
            if (tag.getCode() == code) {
                return tag;
            }
        }
        return null;
    }

    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
