package com.xiaolian.amigo.data.manager;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.os.ParcelUuid;
import android.support.annotation.NonNull;
import android.util.Log;

import com.polidea.rxandroidble.RxBleClient;
import com.polidea.rxandroidble.RxBleConnection;
import com.polidea.rxandroidble.RxBleDevice;
import com.polidea.rxandroidble.scan.ScanFilter;
import com.polidea.rxandroidble.scan.ScanResult;
import com.polidea.rxandroidble.scan.ScanSettings;
import com.polidea.rxandroidble.utils.ConnectionSharingAdapter;
import com.xiaolian.amigo.data.manager.intf.IBleDataManager;

import java.util.UUID;

import javax.inject.Inject;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by caidong on 2017/9/22.
 */
public class BleDataManager implements IBleDataManager {
    private static final String TAG = BleDataManager.class.getSimpleName();
    private static final String SERVICE_UUID = "0000fee9-0000-1000-8000-00805f9b34fb";
    private static final String WRITE_CHARACTERISTIC_UUID = "d44bc439-abfd-45a2-b575-925416129600";
    private RxBleClient client;

    @Inject
    public BleDataManager(RxBleClient client) {
        this.client = client;
    }

    @Override
    public Observable<ScanResult> scan() {
        return client.scanBleDevices(new ScanSettings.Builder().build(),
                new ScanFilter.Builder()
                        // 按照SERVICE_UUID筛选
                        .setServiceUuid(ParcelUuid.fromString(SERVICE_UUID))
                        .build());
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
                .filter(characteristic -> null != characteristic && characteristic.getProperties() == 16);
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
    public Observable<byte[]> write(@NonNull Observable<RxBleConnection> connectionObservable, @NonNull byte[] inputBytes) {
        return connectionObservable
                .flatMap(rxBleConnection -> rxBleConnection.writeCharacteristic(UUID.fromString(WRITE_CHARACTERISTIC_UUID), inputBytes));
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
