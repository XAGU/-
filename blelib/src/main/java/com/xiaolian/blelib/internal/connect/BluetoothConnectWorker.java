package com.xiaolian.blelib.internal.connect;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RestrictTo;
import android.util.Log;

import com.xiaolian.blelib.BluetoothConstants;
import com.xiaolian.blelib.BluetoothHelp;
import com.xiaolian.blelib.connect.BluetoothConnectCallback;
import com.xiaolian.blelib.connect.BluetoothConnectStatusListener;
import com.xiaolian.blelib.connect.BluetoothCharacteristicNotifyCallback;
import com.xiaolian.blelib.connect.BluetoothReadCharacteristicResponse;
import com.xiaolian.blelib.connect.BluetoothReadDescriptorCallback;
import com.xiaolian.blelib.connect.BluetoothWriteCharacteristicCallback;
import com.xiaolian.blelib.connect.BluetoothWriteDescriptorCallback;
import com.xiaolian.blelib.internal.util.SystemVersion;

import java.util.UUID;

/**
 * @author zcd
 * @date 18/3/28
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public class BluetoothConnectWorker implements IBluetoothConnectWorker {
    private static final String TAG = BluetoothConnectWorker.class.getSimpleName();
    private BluetoothGatt bluetoothGatt;
    private BluetoothDevice bluetoothDevice;
    private BluetoothConnectCallback bluetoothConnectCallback;
    private BluetoothConnectStatusListener bluetoothConnectStatusListener;
    private BluetoothCharacteristicNotifyCallback bluetoothCharacteristicNotifyCallback;
    private BluetoothWriteCharacteristicCallback bluetoothWriteCharacteristicCallback;
    private BluetoothReadDescriptorCallback bluetoothReadDescriptorCallback;
    private BluetoothWriteDescriptorCallback bluetoothWriteDescriptorCallback;
    private int connectState = BluetoothConstants.STATE_DISCONNECTED;
    private Handler handler;
    private BluetoothGattCallback coreGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            Log.d(TAG, "BluetoothGattCallback：onConnectionStateChange "
                    + '\n' + "status: " + status
                    + '\n' + "newState: " + newState
                    + '\n' + "currentThread: " + Thread.currentThread().getId()
                    + "name:" + Thread.currentThread().getName());
            if (bluetoothConnectStatusListener != null) {
                handler.post(() -> bluetoothConnectStatusListener.onConnectStatusChanged(newState));
            }
            bluetoothGatt = gatt;
            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (newState == BluetoothGatt.STATE_CONNECTED) {
                    gatt.discoverServices();
                    connectState = BluetoothConstants.STATE_CONNECTING;
                } else if (newState == BluetoothGatt.STATE_DISCONNECTED) {
                    connectState = BluetoothConstants.STATE_DISCONNECTING;
                    closeGatt();
                    handler.post(() -> {
                        if (bluetoothConnectCallback != null) {
                            bluetoothConnectCallback.onResponse(BluetoothConstants.CONN_RESPONSE_FAIL, gatt);
                            // 蓝牙连接回调只回调一遍
                            removeBluetoothConnectCallback();
                        }
                    });
                }
            } else {
                connectState = BluetoothConstants.STATE_DISCONNECTING;
                closeGatt();
                handler.post(() -> {
                    if (bluetoothConnectCallback != null) {
                        bluetoothConnectCallback.onResponse(BluetoothConstants.CONN_RESPONSE_FAIL, gatt);
                        // 蓝牙连接回调只回调一遍
                        removeBluetoothConnectCallback();
                    }
                });
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            Log.d(TAG, "BluetoothGattCallback：onServicesDiscovered "
                    + '\n' + "status: " + status
                    + '\n' + "currentThread: " + Thread.currentThread().getId()
                    + "name:" + Thread.currentThread().getName());
            if (status == BluetoothGatt.GATT_SUCCESS) {
                bluetoothGatt = gatt;
                connectState = BluetoothConstants.STATE_CONNECTED;
                handler.post(() -> {
                    if (bluetoothConnectCallback != null) {
                        bluetoothConnectCallback.onResponse(BluetoothConstants.CONN_RESPONSE_SUCCESS,
                                gatt);
                        // 蓝牙连接回调只回调一遍
                        removeBluetoothConnectCallback();
                    }
                });
            } else {
                closeGatt();
                connectState = BluetoothConstants.STATE_DISCONNECTED;
                handler.post(() -> {
                    if (bluetoothConnectCallback != null) {
                        bluetoothConnectCallback.onResponse(BluetoothConstants.CONN_RESPONSE_FAIL, gatt);
                        // 蓝牙连接回调只回调一遍
                        removeBluetoothConnectCallback();
                    }
                });
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            Log.d(TAG, "BluetoothGattCallback：onCharacteristicRead "
                    + '\n' + "status: " + status
                    + '\n' + "currentThread: " + Thread.currentThread().getId()
                    + "name:" + Thread.currentThread().getName());
            handler.post(() -> {
                if (bluetoothReadDescriptorCallback != null) {
                    bluetoothReadDescriptorCallback.onResponse(status, characteristic.getValue());
                }
            });
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            Log.d(TAG, "BluetoothGattCallback：onCharacteristicWrite "
                    + '\n' + "status: " + status
                    + '\n' + "currentThread: " + Thread.currentThread().getId()
                    + "name:" + Thread.currentThread().getName());
            handler.post(() -> {
                if (bluetoothWriteCharacteristicCallback != null) {
                    bluetoothWriteCharacteristicCallback.onResponse(status);
                }
            });
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            Log.d(TAG, "BluetoothGattCallback：onCharacteristicChanged "
                    + '\n' + "currentThread: " + Thread.currentThread().getId()
                    + "name:" + Thread.currentThread().getName());
            handler.post(() -> {
                if (bluetoothCharacteristicNotifyCallback != null) {
                    bluetoothCharacteristicNotifyCallback.onNotify(characteristic.getValue());
                }
            });
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorRead(gatt, descriptor, status);
            Log.d(TAG, "BluetoothGattCallback：onDescriptorRead "
                    + '\n' + "status: " + status
                    + '\n' + "currentThread: " + Thread.currentThread().getId()
                    + "name:" + Thread.currentThread().getName());
            handler.post(() -> {
                if (bluetoothReadDescriptorCallback != null) {
                    bluetoothReadDescriptorCallback.onResponse(status, descriptor.getValue());
                }
            });
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);
            Log.d(TAG, "BluetoothGattCallback：onDescriptorWrite "
                    + '\n' + "status: " + status
                    + '\n' + "currentThread: " + Thread.currentThread().getId()
                    + "name:" + Thread.currentThread().getName());
            handler.post(() -> {
                if (bluetoothWriteDescriptorCallback != null) {
                    bluetoothWriteDescriptorCallback.onResponse(status);
                }
            });
        }
    };

    private void removeBluetoothConnectCallback() {
        bluetoothConnectCallback = null;
    }


    public BluetoothConnectWorker(String macAddress) {
        handler = new Handler(Looper.getMainLooper());
        BluetoothAdapter adapter = BluetoothHelp.getBluetoothAdapter();
        if (adapter != null) {
            bluetoothDevice = adapter.getRemoteDevice(macAddress);
        } else {
            throw new IllegalStateException("ble adapter null");
        }
    }

    @Override
    public String getMacAddress() {
        if (bluetoothDevice != null) {
            return bluetoothDevice.getAddress();
        }
        return null;
    }

    @Override
    public void addBluetoothConnectCallback(BluetoothConnectCallback bluetoothConnectCallback) {
        this.bluetoothConnectCallback = bluetoothConnectCallback;
    }

    @Override
    public void addBluetoothConnectStatusListener(BluetoothConnectStatusListener bluetoothConnectStatusListener) {
        this.bluetoothConnectStatusListener = bluetoothConnectStatusListener;
    }

    @Override
    public void addBluetoothCharacteristicNotifyCallback(BluetoothCharacteristicNotifyCallback bluetoothCharacteristicNotifyCallback) {
        this.bluetoothCharacteristicNotifyCallback = bluetoothCharacteristicNotifyCallback;
    }

    @Override
    public void addBluetoothWriteCharacteristicCallback(BluetoothWriteCharacteristicCallback bluetoothWriteCharacteristicCallback) {
        this.bluetoothWriteCharacteristicCallback = bluetoothWriteCharacteristicCallback;
    }

    @Override
    public void addBluetoothReadDescriptorCallback(BluetoothReadDescriptorCallback bluetoothReadDescriptorCallback) {
        this.bluetoothReadDescriptorCallback = bluetoothReadDescriptorCallback;
    }

    @Override
    public void addBluetoothWriteDescriptorCallback(BluetoothWriteDescriptorCallback bluetoothWriteDescriptorCallback) {
        this.bluetoothWriteDescriptorCallback = bluetoothWriteDescriptorCallback;
    }

    @Override
    public boolean openGatt() {
        if (bluetoothGatt != null) {
            return true;
        }

        Context context = BluetoothHelp.getContext();
        if (SystemVersion.isMarshmallow()) {
            bluetoothGatt = bluetoothDevice.connectGatt(context, false, coreGattCallback, BluetoothDevice.TRANSPORT_LE);
        } else {
            bluetoothGatt = bluetoothDevice.connectGatt(context, false, coreGattCallback);
        }

        return bluetoothGatt != null;
    }

    @Override
    public void closeGatt() {

        if (bluetoothGatt != null) {
            bluetoothGatt.close();
            bluetoothGatt = null;
        }
        connectState = BluetoothConstants.STATE_DISCONNECTED;

        handler.post(() -> {
            if (bluetoothConnectStatusListener != null) {
                bluetoothConnectStatusListener.onConnectStatusChanged(BluetoothConstants.STATE_CONNECTED);
            }
        });
    }

    @Override
    public boolean discoverService() {
        return bluetoothGatt != null && bluetoothGatt.discoverServices();

    }

    @Override
    public int getCurrentStatus() {
        return connectState;
    }

    @Override
    public boolean setNotify(UUID service, UUID character, boolean enable) {
        BluetoothGattCharacteristic characteristic = getCharacter(service, character);

        if (characteristic == null) {
            return false;
        }

//        if (!isCharacteristicWritable(characteristic)) {
//            BluetoothLog.e(String.format("characteristic not writable!"));
//            return false;
//        }

        return bluetoothGatt != null
                && bluetoothGatt.setCharacteristicNotification(characteristic, enable);

    }

    @Override
    public boolean readCharacteristic(UUID service, UUID character) {
        BluetoothGattCharacteristic characteristic = getCharacter(service, character);

        if (characteristic == null) {
            return false;
        }

//        if (!isCharacteristicReadable(characteristic)) {
//            BluetoothLog.e(String.format("characteristic not readable!"));
//            return false;
//        }

        return bluetoothGatt != null
                && bluetoothGatt.readCharacteristic(characteristic);

    }

    @Override
    public boolean writeCharacteristic(UUID service, UUID character, byte[] value) {
        if (value == null) {
            return false;
        }
        BluetoothGattCharacteristic characteristic = getCharacter(service, character);

        if (characteristic == null) {
            return false;
        }

//        if (!isCharacteristicWritable(characteristic)) {
//            BluetoothLog.e(String.format("characteristic not writable!"));
//            return false;
//        }

        if (bluetoothGatt == null) {
            return false;
        }

        characteristic.setValue(value);

        return bluetoothGatt.writeCharacteristic(characteristic);
    }

    @Override
    public boolean readDescriptor(UUID service, UUID character, UUID descriptor) {
        BluetoothGattCharacteristic characteristic = getCharacter(service, character);

        if (characteristic == null) {
            return false;
        }

        BluetoothGattDescriptor gattDescriptor = characteristic.getDescriptor(descriptor);
        return gattDescriptor != null
                && bluetoothGatt != null
                && bluetoothGatt.readDescriptor(gattDescriptor);

    }

    @Override
    public boolean writeDescriptor(UUID service, UUID character, UUID descriptor, byte[] value) {
        if (value == null) {
            return false;
        }
        BluetoothGattCharacteristic characteristic = getCharacter(service, character);

        if (characteristic == null) {
            return false;
        }

        BluetoothGattDescriptor gattDescriptor = characteristic.getDescriptor(descriptor);
        if (gattDescriptor == null) {
            return false;
        }

        if (bluetoothGatt == null) {
            return false;
        }

        gattDescriptor.setValue(value);

        return bluetoothGatt.writeDescriptor(gattDescriptor);
    }

    @Override
    public boolean writeCharacteristicNoRsp(UUID service, UUID character, byte[] value) {
        if (value == null) {
            return false;
        }
        BluetoothGattCharacteristic characteristic = getCharacter(service, character);

        if (characteristic == null) {
            return false;
        }

//        if (!isCharacteristicNoRspWritable(characteristic)) {
//            BluetoothLog.e(String.format("characteristic not norsp writable!"));
//            return false;
//        }

        if (bluetoothGatt == null) {
            return false;
        }

        characteristic.setValue(value);
        characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);

        return bluetoothGatt.writeCharacteristic(characteristic);
    }

    private BluetoothGattCharacteristic getCharacter(UUID service, UUID character) {
        BluetoothGattCharacteristic characteristic = null;
        if (bluetoothGatt != null) {
            BluetoothGattService gattService = bluetoothGatt.getService(service);
            if (gattService != null) {
                characteristic = gattService.getCharacteristic(character);
            }
        }
        return characteristic;
    }

}
