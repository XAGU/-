package com.xiaolian.blelib.connect;

import java.util.UUID;

/**
 * @author zcd
 * @date 18/3/28
 */

public interface BluetoothCharacteristicNotifyCallback {
    void onResponse(int code);

    void onNotify(byte[] value);
}
