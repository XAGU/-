package com.xiaolian.blelib.internal.connect;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.util.Log;

import com.xiaolian.blelib.BluetoothConstants;
import com.xiaolian.blelib.BluetoothHelp;
import com.xiaolian.blelib.connect.BluetoothConnectCallback;
import com.xiaolian.blelib.connect.BluetoothConnectStatusListener;
import com.xiaolian.blelib.connect.BluetoothCharacteristicNotifyCallback;
import com.xiaolian.blelib.connect.BluetoothReadDescriptorCallback;
import com.xiaolian.blelib.connect.BluetoothWriteCharacteristicCallback;
import com.xiaolian.blelib.connect.BluetoothWriteDescriptorCallback;
import com.xiaolian.blelib.internal.util.SystemVersion;

import java.util.UUID;

/**
 * @author zcd
 * @date 18/3/28
 */

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
    private int connectState = BluetoothConstants.CONNECT_IDLE;
    private BluetoothGattCallback coreGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            Log.d(TAG, "BluetoothGattCallback：onConnectionStateChange "
                    + '\n' + "status: " + status
                    + '\n' + "newState: " + newState
                    + '\n' + "currentThread: " + Thread.currentThread().getId());
            if (newState == BluetoothGatt.STATE_CONNECTED) {
                gatt.discoverServices();
            } else if (newState == BluetoothGatt.STATE_DISCONNECTED) {
                closeGatt();
                if (status == BluetoothConstants.CONNECT_CONNECTING) {
                    connectState = BluetoothConstants.CONNECT_FAILURE;
                    if (bluetoothConnectCallback != null) {
                        bluetoothConnectCallback.onResponse(connectState);
                    }
                } else if (connectState == BluetoothConstants.CONNECT_CONNECTED) {
                    connectState = BluetoothConstants.CONNECT_DISCONNECT;
                    if (bluetoothConnectCallback != null) {
                        bluetoothConnectCallback.onResponse(connectState);
                    }
                }
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            Log.d(TAG, "BluetoothGattCallback：onServicesDiscovered "
                    + '\n' + "status: " + status
                    + '\n' + "currentThread: " + Thread.currentThread().getId());
            if (status == BluetoothGatt.GATT_SUCCESS) {
                bluetoothGatt = gatt;
                connectState = BluetoothConstants.CONNECT_CONNECTED;
                if (bluetoothConnectCallback != null) {
                    bluetoothConnectCallback.onResponse(connectState);
                }
            } else {
                closeGatt();
                connectState = BluetoothConstants.CONNECT_FAILURE;
                if (bluetoothConnectCallback != null) {
                    bluetoothConnectCallback.onResponse(connectState);
                }
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            if (bluetoothReadDescriptorCallback != null) {
                bluetoothReadDescriptorCallback.onResponse(status, characteristic.getValue());
            }
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            if (bluetoothWriteCharacteristicCallback != null) {
                bluetoothWriteCharacteristicCallback.onResponse(status);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            if (bluetoothCharacteristicNotifyCallback != null) {
                bluetoothCharacteristicNotifyCallback.onNotify(characteristic.getValue());
            }
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorRead(gatt, descriptor, status);
            if (bluetoothReadDescriptorCallback != null) {
                bluetoothReadDescriptorCallback.onResponse(status, descriptor.getValue());
            }
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);
            if (bluetoothWriteDescriptorCallback != null) {
                bluetoothWriteDescriptorCallback.onResponse(status);
            }
        }
    };


    public BluetoothConnectWorker(String macAddress) {
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

        if (bluetoothGatt == null) {
            return false;
        }
        return true;
    }

    @Override
    public void closeGatt() {

        if (bluetoothGatt != null) {
            bluetoothGatt.close();
            bluetoothGatt = null;
        }

        if (bluetoothConnectStatusListener != null) {
            bluetoothConnectStatusListener.onConnectStatusChanged(BluetoothConstants.CONNECT_DISCONNECT);
        }
    }

    @Override
    public boolean discoverService() {
        if (bluetoothGatt == null) {
            return false;
        }

        if (!bluetoothGatt.discoverServices()) {
            return false;
        }

        return true;
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

        if (bluetoothGatt == null) {
            return false;
        }

        return bluetoothGatt.setCharacteristicNotification(characteristic, enable);
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

        if (bluetoothGatt == null) {
            return false;
        }

        if (!bluetoothGatt.readCharacteristic(characteristic)) {
            return false;
        }

        return true;
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

        if (!bluetoothGatt.writeCharacteristic(characteristic)) {
            return false;
        }

        return true;
    }

    @Override
    public boolean readDescriptor(UUID service, UUID character, UUID descriptor) {
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

        if (!bluetoothGatt.readDescriptor(gattDescriptor)) {
            return false;
        }

        return true;
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

        if (!bluetoothGatt.writeDescriptor(gattDescriptor)) {
            return false;
        }

        return true;
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

        if (!bluetoothGatt.writeCharacteristic(characteristic)) {
            return false;
        }

        return true;
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
