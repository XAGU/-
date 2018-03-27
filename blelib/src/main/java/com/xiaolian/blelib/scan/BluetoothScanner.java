package com.xiaolian.blelib.scan;

import android.bluetooth.BluetoothAdapter;
import android.support.annotation.CallSuper;

import com.xiaolian.blelib.Constants;
import com.xiaolian.blelib.internal.scan.classic.BluetoothClassicScanner;
import com.xiaolian.blelib.internal.scan.le.BluetoothLEScanner;

/**
 * @author zcd
 * @date 18/3/27
 */

public class BluetoothScanner {
    protected BluetoothAdapter bluetoothAdapter;
    protected BluetoothScanResponse bluetoothScanResponse;

    public static BluetoothScanner newInstance(int type) {
        switch (type) {
            case Constants.SCAN_TYPE_CLASSIC:
                return BluetoothClassicScanner.getInstance();
            case Constants.SCAN_TYPE_BLE:
                return BluetoothLEScanner.getInstance();
            default:
                throw new IllegalArgumentException("illegal type");
        }
    }

    @CallSuper
    protected void startScanBluetooth(BluetoothScanResponse callback) {
        this.bluetoothScanResponse = callback;
        notifyScanStarted();
    }

    @CallSuper
    protected void stopScanBluetooth() {
        notifyScanStopped();
        bluetoothScanResponse = null;
    }

    @CallSuper
    protected void cancelScanBluetooth() {
        notifyScanCanceled();
        bluetoothScanResponse = null;
    }

    private void notifyScanStarted() {
        if (bluetoothScanResponse != null) {
            bluetoothScanResponse.onScanStarted();
        }
    }

    protected void notifyDeviceFounded(BluetoothScanResult scanResult) {
        if (bluetoothScanResponse != null) {
            bluetoothScanResponse.onDeviceFounded(scanResult);
        }
    }

    private void notifyScanStopped() {
        if (bluetoothScanResponse != null) {
            bluetoothScanResponse.onScanStopped();
        }
    }

    private void notifyScanCanceled() {
        if (bluetoothScanResponse != null) {
            bluetoothScanResponse.onScanCanceled();
        }
    }
}
