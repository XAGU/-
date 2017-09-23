package com.xiaolian.amigo.data.manager;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.os.ParcelUuid;

import com.polidea.rxandroidble.RxBleClient;
import com.polidea.rxandroidble.RxBleConnection;
import com.polidea.rxandroidble.RxBleDevice;
import com.polidea.rxandroidble.scan.ScanFilter;
import com.polidea.rxandroidble.scan.ScanResult;
import com.polidea.rxandroidble.scan.ScanSettings;
import com.polidea.rxandroidble.utils.ConnectionSharingAdapter;
import com.xiaolian.amigo.data.manager.intf.IBLEDataManager;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by caidong on 2017/9/22.
 */
public class BLEDataManager implements IBLEDataManager {

    private static final String SERVICE_UUID = "0000fee9-0000-1000-8000-00805f9b34fb";
    private static final String CHARACTERISTIC_UUID = "d44bc439-abfd-45a2-b575-925416129600";
    private RxBleClient client;

    @Inject
    public BLEDataManager(RxBleClient client) {
        this.client = client;
    }

    @Override
    public Observable<ScanResult> scan() {
        return client.scanBleDevices(new ScanSettings.Builder().build(),
                new ScanFilter.Builder()
                        // 按照SERVICE_UUID筛选
//                        .setServiceUuid(ParcelUuid.fromString(SERVICE_UUID))
                        .build());
    }

    @Override
    public Observable<RxBleConnection> prepareConnectionObservable(RxBleDevice device, boolean autoConnect) {
        return device
                .establishConnection(autoConnect)
                .takeUntil(PublishSubject.create())
                .compose(new ConnectionSharingAdapter());
    }

    @Override
    public Observable<BluetoothGattCharacteristic> connect(Observable<RxBleConnection> connectionObservable) {
        return connectionObservable
                .flatMap(RxBleConnection::discoverServices)
                .flatMap(rxBleDeviceServices -> rxBleDeviceServices.getCharacteristic(UUID.fromString(CHARACTERISTIC_UUID)));
    }

    @Override
    public Observable<byte[]> write(Observable<RxBleConnection> connectionObservable, byte[] inputBytes) {
        return connectionObservable
                .flatMap(rxBleConnection -> rxBleConnection.writeCharacteristic(UUID.fromString(CHARACTERISTIC_UUID), inputBytes));
    }
}
