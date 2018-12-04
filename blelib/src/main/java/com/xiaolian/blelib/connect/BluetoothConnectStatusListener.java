package com.xiaolian.blelib.connect;

/**
 * @author zcd
 * @date 18/3/28
 */

public interface BluetoothConnectStatusListener {
    void onConnectStatusChanged(int status , int newStatus);
}
