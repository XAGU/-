package com.xiaolian.blelib.internal.connect;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;

/**
 * @author zcd
 * @date 18/3/28
 */

public class BluetoothGattResponse extends BluetoothGattCallback {
    private IBluetoothGattResponse response;

    public BluetoothGattResponse(IBluetoothGattResponse bluetoothGattResponse) {
        this.response = bluetoothGattResponse;
    }

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        this.response.onConnectionStateChange(status, newState);
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        this.response.onServicesDiscovered(status);
    }

    @Override
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        this.response.onCharacteristicRead(characteristic, status, characteristic.getValue());
    }

    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        this.response.onCharacteristicWrite(characteristic, status, characteristic.getValue());
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        this.response.onCharacteristicChanged(characteristic, characteristic.getValue());
    }

    @Override
    public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        this.response.onDescriptorRead(descriptor, status, descriptor.getValue());
    }

    @Override
    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        this.response.onDescriptorWrite(descriptor, status);
    }
}
