package com.xiaolian.blelib.scan;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.support.annotation.CallSuper;

import com.xiaolian.blelib.BluetoothConstants;
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
            case BluetoothConstants.SCAN_TYPE_CLASSIC:
                return BluetoothClassicScanner.getInstance();
            case BluetoothConstants.SCAN_TYPE_BLE:
                return BluetoothLEScanner.getInstance();
            default:
                throw new IllegalArgumentException("illegal type");
        }
    }

    @CallSuper
    public void startScanBluetooth(BluetoothScanResponse callback) {
        this.bluetoothScanResponse = callback;
        notifyScanStarted();
    }

    @CallSuper
    public void stopScanBluetooth() {
        notifyScanStopped();
        bluetoothScanResponse = null;
    }

    @CallSuper
    public void cancelScanBluetooth() {
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

    protected final BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            notifyDeviceFounded(new BluetoothScanResult(device, rssi, scanRecord));
        }
    };
}
