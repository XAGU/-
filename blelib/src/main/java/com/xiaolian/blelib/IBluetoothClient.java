package com.xiaolian.blelib;

import com.xiaolian.blelib.connect.BluetoothCharacteristicNotifyCallback;
import com.xiaolian.blelib.connect.BluetoothConnectCallback;
import com.xiaolian.blelib.connect.BluetoothConnectStatusListener;
import com.xiaolian.blelib.connect.BluetoothReadDescriptorCallback;
import com.xiaolian.blelib.connect.BluetoothWriteCharacteristicCallback;
import com.xiaolian.blelib.connect.BluetoothWriteDescriptorCallback;
import com.xiaolian.blelib.scan.BluetoothScanResponse;

import java.util.UUID;

/**
 * @author zcd
 * @date 18/3/27
 */

public interface IBluetoothClient {
    void scan(int scanType, BluetoothScanResponse response);
    void stopScan();
    void connect(String macAddress, BluetoothConnectCallback response);
    void disconnect(String mac);
    void registerConnectStatusListener(String mac, BluetoothConnectStatusListener listener);

    void unregisterConnectStatusListener(String mac);

    void read(String mac, UUID service, UUID character, BluetoothReadDescriptorCallback response);

    void write(String mac, UUID service, UUID character, byte[] value, BluetoothWriteCharacteristicCallback response);

    void writeNoRsp(String mac, UUID service, UUID character, byte[] value, BluetoothWriteCharacteristicCallback response);

    void readDescriptor(String mac, UUID service, UUID character, UUID descriptor, BluetoothReadDescriptorCallback response);

    void writeDescriptor(String mac, UUID service, UUID character, UUID descriptor, byte[] value, BluetoothWriteDescriptorCallback response);

    void notify(String mac, UUID service, UUID character, BluetoothCharacteristicNotifyCallback response);

    boolean setNotify(String mac, UUID service, UUID character, boolean enable);

    int getConnectStatus(String mac);
}
