package com.xiaolian.amigo.data.enumeration;

/**
 * 公共浴室相关操作状态
 *
 * @author zcd
 * @date 18/7/13
 */
public enum BathroomOperationStatus {
    SUCCESS(1),
    FAIL(2),
    CANCEL(3),
    OPERATION(4);

    BathroomOperationStatus(int code) {
        this.code = code;
    }

    private int code;

    public int getCode() {
        return code;
    }
}
