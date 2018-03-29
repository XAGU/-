package com.xiaolian.blelib.connect;

import android.bluetooth.BluetoothGatt;

/**
 * @author zcd
 * @date 18/3/28
 */

public interface BluetoothConnectCallback {
    void onResponse(int code, BluetoothGatt bluetoothGatt);
}
