package com.xiaolian.amigo.data.manager;

import com.xiaolian.amigo.data.manager.intf.IBleDataManager;
import com.xiaolian.blelib.IBluetoothClient;
import com.xiaolian.blelib.connect.BluetoothCharacteristicNotifyCallback;
import com.xiaolian.blelib.connect.BluetoothConnectCallback;
import com.xiaolian.blelib.connect.BluetoothConnectStatusListener;
import com.xiaolian.blelib.connect.BluetoothWriteCharacteristicCallback;
import com.xiaolian.blelib.connect.BluetoothWriteDescriptorCallback;
import com.xiaolian.blelib.scan.BluetoothScanResponse;

import java.util.UUID;

import javax.inject.Inject;

/**
 * @author caidong
 * @date 17/9/22
 */
public class BleDataManager implements IBleDataManager {
    private static final String TAG = BleDataManager.class.getSimpleName();
    /**
     * 好年华uuid
     */
    public static final String SERVICE_UUID = "0000fee9-0000-1000-8000-00805f9b34fb";
    public static final String WRITE_CHARACTERISTIC_UUID = "d44bc439-abfd-45a2-b575-925416129600";
    public static final String NOTIFY_DESCRIPTOR_UUID = "00002902-0000-1000-8000-00805f9b34fb";
    private IBluetoothClient bluetoothClient;

    @Inject
    public BleDataManager(IBluetoothClient bluetoothClient) {
        this.bluetoothClient = bluetoothClient;
    }

    @Override
    public void scan(int scanType, BluetoothScanResponse response) {
        bluetoothClient.scan(scanType, response);
    }

    @Override
    public void stopScan() {
        bluetoothClient.stopScan();
    }

    @Override
    public void registerConnectStatusListener(String mac, BluetoothConnectStatusListener listener) {
        bluetoothClient.registerConnectStatusListener(mac, listener);
    }

    @Override
    public void connect(String macAddress, BluetoothConnectCallback response) {
        bluetoothClient.connect(macAddress, response);
    }

    @Override
    public void disconnect(String mac) {
        bluetoothClient.disconnect(mac);
    }

    @Override
    public void unregisterConnectStatusListener(String mac) {
        bluetoothClient.unregisterConnectStatusListener(mac);
    }

    @Override
    public void writeNoRsp(String mac, UUID service, UUID character, byte[] value, BluetoothWriteCharacteristicCallback response) {
        bluetoothClient.writeNoRsp(mac, service, character, value, response);
    }

    @Override
    public boolean setNotify(String mac, UUID service, UUID character, boolean enable) {
        return bluetoothClient.setNotify(mac, service, character, enable);
    }

    @Override
    public void writeDescriptor(String mac, UUID service, UUID character, UUID descriptor, byte[] value, BluetoothWriteDescriptorCallback response) {
        bluetoothClient.writeDescriptor(mac, service, character, descriptor, value, response);
    }

    @Override
    public void notify(String mac, UUID service, UUID character, BluetoothCharacteristicNotifyCallback response) {
        bluetoothClient.notify(mac, service, character, response);
    }

    @Override
    public int getConnectStatus(String mac) {
        return bluetoothClient.getConnectStatus(mac);
    }
}
