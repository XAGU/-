package com.xiaolian.blelib.internal.scan.classic;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.RestrictTo;
import android.support.v4.util.ObjectsCompat;
import android.text.TextUtils;

import com.xiaolian.blelib.BluetoothHelp;
import com.xiaolian.blelib.scan.BluetoothScanResponse;
import com.xiaolian.blelib.scan.BluetoothScanResult;
import com.xiaolian.blelib.scan.BluetoothScanner;

/**
 * @author zcd
 * @date 18/3/27
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public class BluetoothClassicScanner extends BluetoothScanner {
    private BluetoothScanReceiver receiver;

    public BluetoothClassicScanner() {
        bluetoothAdapter = BluetoothHelp.getBluetoothAdapter();
    }

    public static BluetoothClassicScanner getInstance() {
        return BluetoothClassicScannerHolder.instance;
    }

    private static class BluetoothClassicScannerHolder {
        private static BluetoothClassicScanner instance = new BluetoothClassicScanner();
    }

    @Override
    public void startScanBluetooth(BluetoothScanResponse callback) {
        super.startScanBluetooth(callback);
        registerReceiver();
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        bluetoothAdapter.startDiscovery();
    }

    @Override
    public void stopScanBluetooth() {
        unregisterReceiver();
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        super.stopScanBluetooth();
    }

    @Override
    public void cancelScanBluetooth() {
        unregisterReceiver();
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        super.cancelScanBluetooth();
    }

    private void registerReceiver() {
        if (receiver == null) {
            receiver = new BluetoothScanReceiver();
            BluetoothHelp.registerReceiver(receiver,
                    new IntentFilter(BluetoothDevice.ACTION_FOUND));
        }
    }

    private void unregisterReceiver() {
        if (receiver != null) {
            BluetoothHelp.unregisterReceiver(receiver);
            receiver = null;
        }
    }

    private class BluetoothScanReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (TextUtils.equals(intent.getAction(), BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,
                        Short.MIN_VALUE);
                // 使用这种方式扫描到的设备scanRecord为空
                BluetoothScanResult xmDevice = new BluetoothScanResult(device,
                        rssi, null);
                notifyDeviceFounded(xmDevice);
            }
        }
    }
}
