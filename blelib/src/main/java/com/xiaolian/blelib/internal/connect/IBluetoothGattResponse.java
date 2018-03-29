package com.xiaolian.blelib.internal.connect;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;

/**
 * @author zcd
 * @date 18/3/28
 */

public interface IBluetoothGattResponse {
    void onConnectionStateChange(int status, int newState);

    void onServicesDiscovered(int status);

    void onCharacteristicRead(BluetoothGattCharacteristic characteristic, int status, byte[] value);

    void onCharacteristicWrite(BluetoothGattCharacteristic characteristic, int status, byte[] value);

    void onCharacteristicChanged(BluetoothGattCharacteristic characteristic, byte[] value);

    void onDescriptorRead(BluetoothGattDescriptor descriptor, int status, byte[] value);

    void onDescriptorWrite(BluetoothGattDescriptor descriptor, int status);
}
