package com.xiaolian.blelib.internal.scan.classic;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.RestrictTo;
import android.support.v4.util.ObjectsCompat;
import android.text.TextUtils;

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
    }

    public static BluetoothClassicScanner getInstance() {
        return BluetoothClassicScannerHolder.instance;
    }

    private static class BluetoothClassicScannerHolder {
        private static BluetoothClassicScanner instance = new BluetoothClassicScanner();
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
