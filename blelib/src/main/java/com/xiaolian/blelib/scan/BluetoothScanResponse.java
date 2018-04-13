package com.xiaolian.blelib.scan;

import android.support.annotation.MainThread;

/**
 * @author zcd
 * @date 18/3/27
 */

public interface BluetoothScanResponse {
    @MainThread
    void onScanStarted();

    @MainThread
    void onDeviceFounded(BluetoothScanResult scanResult);

    @MainThread
    void onScanStopped();

    @MainThread
    void onScanCanceled();
}
