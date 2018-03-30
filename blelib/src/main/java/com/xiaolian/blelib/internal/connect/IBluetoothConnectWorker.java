package com.xiaolian.blelib.internal.connect;

import android.support.annotation.RestrictTo;

import com.xiaolian.blelib.connect.BluetoothCharacteristicNotifyCallback;
import com.xiaolian.blelib.connect.BluetoothConnectCallback;
import com.xiaolian.blelib.connect.BluetoothConnectStatusListener;
import com.xiaolian.blelib.connect.BluetoothReadDescriptorCallback;
import com.xiaolian.blelib.connect.BluetoothWriteCharacteristicCallback;
import com.xiaolian.blelib.connect.BluetoothWriteDescriptorCallback;

import java.util.UUID;

/**
 * @author zcd
 * @date 18/3/28
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public interface IBluetoothConnectWorker {

    String getMacAddress();

    void addBluetoothConnectCallback(BluetoothConnectCallback bluetoothConnectCallback);

    void addBluetoothConnectStatusListener(BluetoothConnectStatusListener bluetoothConnectStatusListener);

    void addBluetoothCharacteristicNotifyCallback(BluetoothCharacteristicNotifyCallback bluetoothCharacteristicNotifyCallback);

    void addBluetoothWriteCharacteristicCallback(BluetoothWriteCharacteristicCallback bluetoothWriteCharacteristicCallback);

    void addBluetoothReadDescriptorCallback(BluetoothReadDescriptorCallback bluetoothReadDescriptorCallback);

    void addBluetoothWriteDescriptorCallback(BluetoothWriteDescriptorCallback bluetoothWriteDescriptorCallback);

    boolean openGatt();

    void closeGatt();

    boolean discoverService();

    int getCurrentStatus();

    boolean setNotify(UUID service, UUID characteristic, boolean enable);

    boolean readCharacteristic(UUID service, UUID characteristic);

    boolean writeCharacteristic(UUID service, UUID character, byte[] value);

    boolean readDescriptor(UUID service, UUID characteristic, UUID descriptor);

    boolean writeDescriptor(UUID service, UUID characteristic, UUID descriptor, byte[] value);

    boolean writeCharacteristicNoRsp(UUID service, UUID characteristic, byte[] value);
}
