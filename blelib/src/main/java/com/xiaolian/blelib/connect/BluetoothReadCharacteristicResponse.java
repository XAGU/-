package com.xiaolian.blelib.connect;

/**
 * @author zcd
 * @date 18/3/28
 */

public interface BluetoothReadCharacteristicResponse {
    void onResponse(int code, byte[] data);
}
