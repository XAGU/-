package com.xiaolian.blelib;

import android.content.Context;

import com.xiaolian.blelib.scan.BluetoothScanResponse;
import com.xiaolian.blelib.scan.BluetoothScanner;

/**
 * @author zcd
 * @date 18/3/27
 */

public class BluetoothClient implements IBluetoothClient {
    private BluetoothScanner bluetoothScanner;

    public BluetoothClient(Context context) {
        if (context == null) {
            throw new NullPointerException("context can't be null");
        }
        BluetoothContext.set(context);
    }

    @Override
    public void scan(int scanType, BluetoothScanResponse response) {
        bluetoothScanner = BluetoothScanner.newInstance(scanType);
        bluetoothScanner.startScanBluetooth(response);
    }

    @Override
    public void stopScan() {
        if (bluetoothScanner != null) {
            bluetoothScanner.stopScanBluetooth();
        }
    }

}
