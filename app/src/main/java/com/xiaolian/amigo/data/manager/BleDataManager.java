package com.xiaolian.amigo.data.manager;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.support.annotation.NonNull;

import com.polidea.rxandroidble.RxBleClient;
import com.polidea.rxandroidble.RxBleConnection;
import com.polidea.rxandroidble.RxBleDevice;
import com.polidea.rxandroidble.scan.ScanFilter;
import com.polidea.rxandroidble.scan.ScanResult;
import com.polidea.rxandroidble.scan.ScanSettings;
import com.polidea.rxandroidble.utils.ConnectionSharingAdapter;
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

import rx.Observable;
import rx.subjects.PublishSubject;

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
    private RxBleClient client;
    private IBluetoothClient bluetoothClient;

    @Inject
    public BleDataManager(RxBleClient client, IBluetoothClient bluetoothClient) {
        this.client = client;
        this.bluetoothClient = bluetoothClient;
    }

    @Override
    public Observable<ScanResult> scan() {
        return client.scanBleDevices(new ScanSettings.Builder()
                        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
//                        .setCallbackType(ScanSettings.CALLBACK_TYPE_FIRST_MATCH)
                        .build(),
                new ScanFilter.Builder()
                        // 按照SERVICE_UUID筛选
                        //.setServiceUuid(ParcelUuid.fromString(SERVICE_UUID))
                        .build());
    }

    @Override
    public Observable<ScanResult> scan(String deviceName) {
        return client.scanBleDevices(
                new ScanSettings.Builder()
                        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                        .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
                        .build(),
                new ScanFilter.Builder()
                        .setDeviceName(deviceName)
                        .build());
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

    @Override
    public Observable<RxBleConnection> prepareConnectionObservable(@NonNull String macAddress, boolean autoConnect, @NonNull PublishSubject<Void> disconnectTriggerSubject) {
        RxBleDevice device = client.getBleDevice(macAddress);
        return device
                .establishConnection(autoConnect)
                .takeUntil(disconnectTriggerSubject)
                .compose(new ConnectionSharingAdapter());
    }

    @Override
    public Observable<BluetoothGattCharacteristic> connect(Observable<RxBleConnection> connectionObservable) {
        return connectionObservable
                .flatMap(RxBleConnection::discoverServices)
                .flatMap(rxBleDeviceServices -> Observable.from(rxBleDeviceServices.getBluetoothGattServices()))
                .flatMap(bluetoothGattService -> Observable.from(bluetoothGattService.getCharacteristics()))
                // notify特征值属性必须为16
                .filter(characteristic -> null != characteristic
                        && characteristic.getProperties() == 16
                        && SERVICE_UUID.equals(characteristic.getService().getUuid().toString()));
    }

    @Override
    public Observable<Observable<byte[]>> connect2(Observable<RxBleConnection> connectionObservable, String notifyCharacteristicUuid) {
        return connectionObservable
                .flatMap(rxBleConnection -> rxBleConnection.setupNotification(UUID.fromString(notifyCharacteristicUuid)));
    }

    @Override
    public Observable<Observable<byte[]>> setupNotification(Observable<RxBleConnection> connectionObservable, BluetoothGattCharacteristic characteristic) {
        return connectionObservable.flatMap(rxBleConnection -> rxBleConnection.setupNotification(characteristic));
    }

    @Override
    public Observable<byte[]> writeDescriptor(@NonNull Observable<RxBleConnection> connectionObservable, @NonNull BluetoothGattDescriptor descriptor) {
        BluetoothGattCharacteristic characteristic = descriptor.getCharacteristic();
        return connectionObservable
                .flatMap(rxBleConnection -> rxBleConnection.writeDescriptor(descriptor, BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE));
    }

    @Override
    public Observable<byte[]> write(@NonNull Observable<RxBleConnection> connectionObservable, @NonNull byte[] inputBytes, String writeCharacteristicUuid) {
        return connectionObservable
                .flatMap(rxBleConnection -> rxBleConnection.writeCharacteristic(UUID.fromString(writeCharacteristicUuid), inputBytes));
    }

    @Override
    public Observable<byte[]> notify(@NonNull Observable<RxBleConnection> connectionObservable, BluetoothGattCharacteristic characteristic) {
        return connectionObservable
                .flatMap(rxBleConnection -> rxBleConnection.setupNotification(characteristic))
                .flatMap(notificationObservable -> notificationObservable);
    }

    @Override
    public Observable<RxBleConnection.RxBleConnectionState> monitorStatus(@NonNull String macAddress) {
        RxBleDevice device = client.getBleDevice(macAddress);
        return device.observeConnectionStateChanges();
    }

    @Override
    public RxBleConnection.RxBleConnectionState getStatus(@NonNull String macAddress) {
        RxBleDevice device = client.getBleDevice(macAddress);
        return device.getConnectionState();
    }
}
