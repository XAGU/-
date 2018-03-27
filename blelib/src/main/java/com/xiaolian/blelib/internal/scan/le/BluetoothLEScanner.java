package com.xiaolian.blelib.internal.scan.le;

import android.support.annotation.RestrictTo;

import com.xiaolian.blelib.scan.BluetoothScanner;

/**
 * @author zcd
 * @date 18/3/27
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public class BluetoothLEScanner extends BluetoothScanner {

    public static BluetoothLEScanner getInstance() {
        return BluetoothLEScannerHolder.instance;
    }

    private static class BluetoothLEScannerHolder {
        private static BluetoothLEScanner instance = new BluetoothLEScanner();
    }
}
