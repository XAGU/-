package com.xiaolian.amigo.data.manager.intf;

import android.bluetooth.BluetoothGattCharacteristic;

import com.polidea.rxandroidble.RxBleConnection;
import com.polidea.rxandroidble.RxBleDevice;
import com.polidea.rxandroidble.RxBleDeviceServices;
import com.polidea.rxandroidble.scan.ScanResult;

import rx.Observable;

/**
 * 蓝牙相关操作
 * <p>
 * Created by caidong on 2017/9/22.
 */
public interface IBLEDataManager {

    // 扫描设备
    Observable<ScanResult> scan();

    /**
     * 提供连接蓝牙设备的Observable，附带连接共享适配器供后续蓝牙操作使用
     *
     * @param device      蓝牙设备
     * @param autoConnect 是否自动连接
     */
    Observable<RxBleConnection> prepareConnectionObservable(RxBleDevice device, boolean autoConnect);

    // 连接蓝牙
    Observable<BluetoothGattCharacteristic> connect(Observable<RxBleConnection> connectionObservable);

    /**
     * 向蓝牙设备发送数据
     *
     * @param connectionObservable 连接句柄
     * @param inputBytes           待发送的数据
     */
    Observable<byte[]> write(Observable<RxBleConnection> connectionObservable, byte[] inputBytes);
}
