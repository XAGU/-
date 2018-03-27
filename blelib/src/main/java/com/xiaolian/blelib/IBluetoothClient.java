package com.xiaolian.blelib;

import com.xiaolian.blelib.scan.BluetoothScanResponse;

/**
 * @author zcd
 * @date 18/3/27
 */

public interface IBluetoothClient {
    void scan(int scanType, BluetoothScanResponse response);
    void stopScan();
}
