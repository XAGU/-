package com.xiaolian.amigo.data.manager.intf;

import com.xiaolian.blelib.connect.BluetoothCharacteristicNotifyCallback;
import com.xiaolian.blelib.connect.BluetoothConnectCallback;
import com.xiaolian.blelib.connect.BluetoothConnectStatusListener;
import com.xiaolian.blelib.connect.BluetoothWriteCharacteristicCallback;
import com.xiaolian.blelib.connect.BluetoothWriteDescriptorCallback;
import com.xiaolian.blelib.scan.BluetoothScanResponse;

import java.util.UUID;

/**
 * 蓝牙相关操作
 *
 * @author caidong
 * @date 17/9/22
 */
public interface IBleDataManager {
    void scan(BluetoothScanResponse response);

    void stopScan();

    void registerConnectStatusListener(String mac, BluetoothConnectStatusListener listener);

    void connect(final String macAddress, BluetoothConnectCallback response);

    void disconnect(String mac);

    void unregisterConnectStatusListener(String mac);

    void writeNoRsp(String mac, UUID service, UUID character, byte[] value, BluetoothWriteCharacteristicCallback response);

    boolean setNotify(String mac, UUID service, UUID character, boolean enable);

    void writeDescriptor(String mac, UUID service, UUID character, UUID descriptor, byte[] value,
                         BluetoothWriteDescriptorCallback response);

    void notify(String mac, UUID service, UUID character, BluetoothCharacteristicNotifyCallback response);

    int getConnectStatus(String mac);
}
