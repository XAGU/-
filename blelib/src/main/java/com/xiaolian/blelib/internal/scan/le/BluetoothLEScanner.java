package com.xiaolian.blelib.internal.scan.le;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.support.annotation.RestrictTo;
import android.util.Log;

import com.xiaolian.blelib.BluetoothHelp;
import com.xiaolian.blelib.scan.BluetoothScanResponse;
import com.xiaolian.blelib.scan.BluetoothScanResult;
import com.xiaolian.blelib.scan.BluetoothScanner;

/**
 * @author zcd
 * @date 18/3/27
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public class BluetoothLEScanner extends BluetoothScanner {

    public BluetoothLEScanner() {
        bluetoothAdapter = BluetoothHelp.getBluetoothAdapter();
    }

    public static BluetoothLEScanner getInstance() {
        return BluetoothLEScannerHolder.instance;
    }

    private static class BluetoothLEScannerHolder {
        private static BluetoothLEScanner instance = new BluetoothLEScanner();
    }

    @Override
    public void startScanBluetooth(BluetoothScanResponse callback) {
        super.startScanBluetooth(callback);
        bluetoothAdapter.startLeScan(leScanCallback);
    }

    @Override
    public void stopScanBluetooth() {
        try {
            bluetoothAdapter.stopLeScan(leScanCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.stopScanBluetooth();
    }

    @Override
    public void cancelScanBluetooth() {
        bluetoothAdapter.stopLeScan(leScanCallback);
        super.cancelScanBluetooth();
    }

    private final BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            notifyDeviceFounded(new BluetoothScanResult(device, rssi, scanRecord));
        }
    };
}
