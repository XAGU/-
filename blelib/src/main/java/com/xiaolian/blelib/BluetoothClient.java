package com.xiaolian.blelib;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

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
    private static final String TAG = BluetoothClient.class.getSimpleName();
    private BluetoothScanner bluetoothScanner;
    private BluetoothScanner bluetoothScannerClassic;
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
        bluetoothScanner = BluetoothScanner.newInstance(BluetoothConstants.SCAN_TYPE_BLE);
        bluetoothScanner.startScanBluetooth(response);

        bluetoothScannerClassic = BluetoothScanner.newInstance(BluetoothConstants.SCAN_TYPE_CLASSIC);
        bluetoothScannerClassic.startScanBluetooth(response);
    }

    @Override
    public void stopScan() {
        if (bluetoothScanner != null) {
            bluetoothScanner.stopScanBluetooth();
            bluetoothScanner = null;
        }

        if (bluetoothScannerClassic != null) {
            bluetoothScannerClassic.stopScanBluetooth();
            bluetoothScannerClassic = null;
        }
    }

    @Override
    public synchronized void connect(final String macAddress, BluetoothConnectCallback response) {
        if (bluetoothConnectWorker != null) {
            if (bluetoothConnectWorker.getCurrentStatus() == BluetoothConstants.STATE_CONNECTED) {
                Log.d(TAG, "[connect]bluetoothConnectWorker不为空，且STATE_CONNECTED, 关闭GATT");
//                bluetoothConnectWorker.closeGatt();
                // 不发送关闭Gatt消息
                bluetoothConnectWorker.closeGattNoSendListener();
                handler.postDelayed(() -> {
                    Log.d(TAG, "[connect]500毫秒延时后重新开启GATT");
                    bluetoothConnectWorker = new BluetoothConnectWorker(macAddress);
                    openGatt(macAddress, response);
                }, 500);
            } else {
                Log.d(TAG, "[connect]bluetoothConnectWorker不为空，且不是连接状态, 开启GATT");
                openGatt(macAddress, response);
            }
        } else {
            Log.d(TAG, "[connect]bluetoothConnectWorker为空，初始化，开启GATT");
            bluetoothConnectWorker = new BluetoothConnectWorker(macAddress);
            openGatt(macAddress, response);
        }
    }

    private synchronized void openGatt(final String macAddress, BluetoothConnectCallback response) {
        if (!TextUtils.equals(macAddress, bluetoothConnectWorker.getMacAddress())) {
            Log.d(TAG, "[openGatt]mac不相同，设置mac，原mac为" + bluetoothConnectWorker.getMacAddress()
            + "现mac为" + macAddress);
            bluetoothConnectWorker.setMacAddress(macAddress);
        }
        bluetoothConnectWorker.addBluetoothConnectCallback(response);
        if (!bluetoothConnectWorker.openGatt()) {
            Log.d(TAG, "[openGatt]openGatt失败");
            if (bluetoothConnectWorker != null) {
                bluetoothConnectWorker.addBluetoothConnectCallback(null);
            }
            response.onResponse(BluetoothConstants.CONN_RESPONSE_FAIL, null);
        }
    }

    @Override
    public synchronized void disconnect(String mac) {
//        checkMacAddress(mac);
        Log.d(TAG, "[disconnect]关闭连接" + mac);
        if (bluetoothConnectWorker != null) {
            bluetoothConnectWorker.closeGatt();
            bluetoothConnectWorker = null;
        }
    }

    @Override
    public synchronized void registerConnectStatusListener(String mac, BluetoothConnectStatusListener listener) {
        if (bluetoothConnectWorker == null) {
            bluetoothConnectWorker = new BluetoothConnectWorker(mac);
        }
        if (!checkMacAddress(mac)) {
            Log.d(TAG, "[registerConnectStatusListener]注册连接状态监听失败 " + bluetoothConnectWorker.toString());
            return;
        }
        Log.d(TAG, "[registerConnectStatusListener]注册连接状态监听 " + bluetoothConnectWorker.toString());
        bluetoothConnectWorker.addBluetoothConnectStatusListener(listener);
    }

    @Override
    public synchronized void unregisterConnectStatusListener(String mac) {
//        checkMacAddress(mac);
        if (bluetoothConnectWorker != null) {
            Log.d(TAG, "取消连接状态监听 " + bluetoothConnectWorker.toString());
            bluetoothConnectWorker.addBluetoothConnectStatusListener(null);
        }
    }

    @Override
    public synchronized void read(String mac, UUID service, UUID character, BluetoothReadDescriptorCallback response) {
        if (!checkMacAddress(mac)) {
            Log.d(TAG, "[read]check mac失败" + mac + " " + (bluetoothConnectWorker == null
                    ? "worker为null" : bluetoothConnectWorker.getMacAddress()));
            response.onResponse(BluetoothConstants.GATT_OTHER_FAILURE, null);
            return;
        }
        bluetoothConnectWorker.addBluetoothReadDescriptorCallback(response);
        if (!bluetoothConnectWorker.readCharacteristic(service, character)) {
            if (bluetoothConnectWorker != null) {
                bluetoothConnectWorker.addBluetoothReadDescriptorCallback(null);
            }
            response.onResponse(BluetoothConstants.GATT_OTHER_FAILURE, null);
        }
    }

    @Override
    public synchronized void write(String mac, UUID service, UUID character, byte[] value, BluetoothWriteCharacteristicCallback response) {
        if (!checkMacAddress(mac)) {
            Log.d(TAG, "[write]check mac失败" + mac + " " + (bluetoothConnectWorker == null
                    ? "worker为null" : bluetoothConnectWorker.getMacAddress()));
            response.onResponse(BluetoothConstants.GATT_OTHER_FAILURE);
            return;
        }
        bluetoothConnectWorker.addBluetoothWriteCharacteristicCallback(response);
        if (!bluetoothConnectWorker.writeCharacteristic(service, character, value)) {
            if (bluetoothConnectWorker != null) {
                bluetoothConnectWorker.addBluetoothWriteCharacteristicCallback(null);
            }
            response.onResponse(BluetoothConstants.GATT_OTHER_FAILURE);
        }
    }

    @Override
    public synchronized void writeNoRsp(String mac, UUID service, UUID character, byte[] value, BluetoothWriteCharacteristicCallback response) {
        if (!checkMacAddress(mac)) {
            Log.d(TAG, "[writeNoRsp]check mac失败" + mac + " " + (bluetoothConnectWorker == null
                    ? "worker为null" : bluetoothConnectWorker.getMacAddress()));
            response.onResponse(BluetoothConstants.GATT_OTHER_FAILURE);
            return;
        }
        if (!bluetoothConnectWorker.writeCharacteristicNoRsp(service, character, value)) {
            response.onResponse(BluetoothConstants.GATT_OTHER_FAILURE);
        } else {
            response.onResponse(BluetoothConstants.GATT_SUCCESS);
        }
    }

    private synchronized boolean checkMacAddress(String mac) {
        return bluetoothConnectWorker != null
                && TextUtils.equals(bluetoothConnectWorker.getMacAddress(), mac);
    }

    @Override
    public synchronized void readDescriptor(String mac, UUID service, UUID character, UUID descriptor, BluetoothReadDescriptorCallback response) {
        if (!checkMacAddress(mac)) {
            Log.d(TAG, "[readDescriptor]check mac失败" + mac + " " + (bluetoothConnectWorker == null
                    ? "worker为null" : bluetoothConnectWorker.getMacAddress()));
            response.onResponse(BluetoothConstants.GATT_OTHER_FAILURE, null);
            return;
        }
        bluetoothConnectWorker.addBluetoothReadDescriptorCallback(response);
        if (!bluetoothConnectWorker.readDescriptor(service, character, descriptor)) {
            if (bluetoothConnectWorker != null) {
                bluetoothConnectWorker.addBluetoothReadDescriptorCallback(null);
            }
            response.onResponse(BluetoothConstants.GATT_OTHER_FAILURE, null);
        }
    }

    @Override
    public synchronized void writeDescriptor(String mac, UUID service, UUID character, UUID descriptor, byte[] value, BluetoothWriteDescriptorCallback response) {
        if (!checkMacAddress(mac)) {
            Log.d(TAG, "[writeDescriptor]check mac失败" + mac + " " + (bluetoothConnectWorker == null
                    ? "worker为null" : bluetoothConnectWorker.getMacAddress()));
            response.onResponse(BluetoothConstants.GATT_OTHER_FAILURE);
            return;
        }
        bluetoothConnectWorker.addBluetoothWriteDescriptorCallback(response);
        if (!bluetoothConnectWorker.writeDescriptor(service, character, descriptor, value)) {
            if (bluetoothConnectWorker != null) {
                bluetoothConnectWorker.addBluetoothWriteDescriptorCallback(null);
            }
            response.onResponse(BluetoothConstants.GATT_OTHER_FAILURE);
        }

    }

    @Override
    public synchronized void notify(String mac, UUID service, UUID character, BluetoothCharacteristicNotifyCallback response) {
        if (!checkMacAddress(mac)) {

//            Log.d(TAG, "[notify]check mac失败" + mac + " " + bluetoothConnectWorker == null
//                    ? "worker为null" : bluetoothConnectWorker.getMacAddress());

            Log.d(TAG, "[notify]check mac失败" + mac);
            return;
        }
        if (bluetoothConnectWorker != null) {
            bluetoothConnectWorker.addBluetoothCharacteristicNotifyCallback(response);
        }
    }

    @Override
    public synchronized boolean setNotify(String mac, UUID service, UUID character, boolean enable) {
        if (!checkMacAddress(mac)) {
            Log.d(TAG, "[setNotify]check mac失败" + mac + " " + (bluetoothConnectWorker == null
                    ? "worker为null" : bluetoothConnectWorker.getMacAddress()));
            return false;
        }
        return bluetoothConnectWorker != null
            && bluetoothConnectWorker.setNotify(service, character, enable);
    }

    @Override
    public synchronized int getConnectStatus(String mac) {
        if (!checkMacAddress(mac)) {
            Log.d(TAG, "[getConnectStatus]check mac失败" + mac + " " + (bluetoothConnectWorker == null
                    ? "worker为null" : bluetoothConnectWorker.getMacAddress()));
            return BluetoothConstants.STATE_DISCONNECTED;
        }
        return bluetoothConnectWorker.getCurrentStatus();
    }

}
