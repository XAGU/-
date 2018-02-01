package com.xiaolian.amigo.data.enumeration;

/**
 * error tag
 *
 * @author caidong
 * @date 17/10/13
 */
public enum ErrorTag {
    // FIXME 增加设备时需要更改此处
    CONNECT_ERROR(1), DEVICE_BUSY(2), REPAIR(3), CALL(4), CHANGE_DORMITORY(5), CHANGE_DISPENSER(6), CHANGE_DRYER(7);

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
