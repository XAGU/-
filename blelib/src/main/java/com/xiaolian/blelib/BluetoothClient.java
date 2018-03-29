package com.xiaolian.blelib;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.xiaolian.blelib.connect.BluetoothCharacteristicNotifyCallback;
import com.xiaolian.blelib.connect.BluetoothConnectCallback;
import com.xiaolian.blelib.connect.BluetoothConnectStatusListener;
import com.xiaolian.blelib.connect.BluetoothReadDescriptorCallback;
import com.xiaolian.blelib.connect.BluetoothWriteCharacteristicCallback;
import com.xiaolian.blelib.connect.BluetoothWriteDescriptorCallback;
import com.xiaolian.blelib.internal.connect.BluetoothConnectWorker;
import com.xiaolian.blelib.internal.connect.IBluetoothConnectWorker;
import com.xiaolian.blelib.scan.BluetoothScanResponse;
import com.xiaolian.blelib.scan.BluetoothScanner;

import java.util.UUID;

/**
 * @author zcd
 * @date 18/3/27
 */

public class BluetoothClient implements IBluetoothClient {
    private BluetoothScanner bluetoothScanner;
    private IBluetoothConnectWorker bluetoothConnectWorker;
    private Handler handler;

    public BluetoothClient(Context context) {
        if (context == null) {
            throw new NullPointerException("context can't be null");
        }
        handler = new Handler(Looper.getMainLooper());
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

    @Override
    public void connect(final String macAddress, BluetoothConnectCallback response) {
        if (bluetoothConnectWorker != null) {
            if (bluetoothConnectWorker.getCurrentStatus() == BluetoothConstants.CONN_STATE_CONNECTED) {
                bluetoothConnectWorker.closeGatt();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bluetoothConnectWorker = new BluetoothConnectWorker(macAddress);
                    }
                }, 1000);
            } else {
                openGatt(macAddress, response);
            }
        } else {
            openGatt(macAddress, response);
        }
    }

    private void openGatt(final String macAddress, BluetoothConnectCallback response) {
        bluetoothConnectWorker = new BluetoothConnectWorker(macAddress);
        bluetoothConnectWorker.addBluetoothConnectCallback(response);
        if (!bluetoothConnectWorker.openGatt()) {
            response.onResponse(BluetoothConstants.CONN_RESPONSE_FAIL, null);
            bluetoothConnectWorker.addBluetoothConnectCallback(null);
        }
    }

    @Override
    public void disconnect(String mac) {
        checkMacAddress(mac);
        if (bluetoothConnectWorker != null) {
            bluetoothConnectWorker.closeGatt();
            bluetoothConnectWorker = null;
        }
    }

    @Override
    public void registerConnectStatusListener(String mac, BluetoothConnectStatusListener listener) {
        if (bluetoothConnectWorker == null) {
            bluetoothConnectWorker = new BluetoothConnectWorker(mac);
        }
        checkMacAddress(mac);
        bluetoothConnectWorker.addBluetoothConnectStatusListener(listener);
    }

    @Override
    public void unregisterConnectStatusListener(String mac) {
        checkMacAddress(mac);
        bluetoothConnectWorker.addBluetoothConnectStatusListener(null);
    }

    @Override
    public void read(String mac, UUID service, UUID character, BluetoothReadDescriptorCallback response) {
        checkMacAddress(mac);
        bluetoothConnectWorker.addBluetoothReadDescriptorCallback(response);
        if (!bluetoothConnectWorker.readCharacteristic(service, character)) {
            response.onResponse(BluetoothConstants.GATT_OTHER_FAILURE, null);
            bluetoothConnectWorker.addBluetoothReadDescriptorCallback(null);
        }
    }

    @Override
    public void write(String mac, UUID service, UUID character, byte[] value, BluetoothWriteCharacteristicCallback response) {
        checkMacAddress(mac);
        bluetoothConnectWorker.addBluetoothWriteCharacteristicCallback(response);
        if (!bluetoothConnectWorker.writeCharacteristic(service, character, value)) {
            response.onResponse(BluetoothConstants.GATT_OTHER_FAILURE);
            bluetoothConnectWorker.addBluetoothWriteCharacteristicCallback(null);
        }
    }

    @Override
    public void writeNoRsp(String mac, UUID service, UUID character, byte[] value, BluetoothWriteCharacteristicCallback response) {
        checkMacAddress(mac);
        if (!bluetoothConnectWorker.writeCharacteristicNoRsp(service, character, value)) {
            response.onResponse(BluetoothConstants.GATT_OTHER_FAILURE);
        } else {
            response.onResponse(BluetoothConstants.GATT_SUCCESS);
        }
    }

    private void checkMacAddress(String mac) {
        if (bluetoothConnectWorker == null) {
            return;
        }
        if (!TextUtils.equals(bluetoothConnectWorker.getMacAddress(), mac)) {
            throw new IllegalStateException("different mac");
        }
    }

    @Override
    public void readDescriptor(String mac, UUID service, UUID character, UUID descriptor, BluetoothReadDescriptorCallback response) {
        checkMacAddress(mac);
        bluetoothConnectWorker.addBluetoothReadDescriptorCallback(response);
        if (!bluetoothConnectWorker.readDescriptor(service, character, descriptor)) {
            response.onResponse(BluetoothConstants.GATT_OTHER_FAILURE, null);
            bluetoothConnectWorker.addBluetoothReadDescriptorCallback(null);
        }
    }

    @Override
    public void writeDescriptor(String mac, UUID service, UUID character, UUID descriptor, byte[] value, BluetoothWriteDescriptorCallback response) {
        checkMacAddress(mac);
        bluetoothConnectWorker.addBluetoothWriteDescriptorCallback(response);
        if (!bluetoothConnectWorker.writeDescriptor(service, character, descriptor, value)) {
            response.onResponse(BluetoothConstants.GATT_OTHER_FAILURE);
            bluetoothConnectWorker.addBluetoothWriteDescriptorCallback(null);
        }

    }

    @Override
    public void notify(String mac, UUID service, UUID character, BluetoothCharacteristicNotifyCallback response) {
        checkMacAddress(mac);
        bluetoothConnectWorker.addBluetoothCharacteristicNotifyCallback(response);
    }

    @Override
    public boolean setNotify(String mac, UUID service, UUID character, boolean enable) {
        checkMacAddress(mac);
        return bluetoothConnectWorker.setNotify(service, character, enable);
    }

    @Override
    public int getConnectStatus(String mac) {
        checkMacAddress(mac);
        return bluetoothConnectWorker.getCurrentStatus();
    }

}
