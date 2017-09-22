package com.xiaolian.amigo.ble;

/**
 * 蓝牙操作异常
 * <p>
 * Created by caidong on 2017/9/22.
 */
public class BLEException extends RuntimeException {

    public BLEException(String msg) {
        super(msg);
    }

    public BLEException(String msg, Throwable e) {
        super(msg, e);
    }
}
