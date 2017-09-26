package com.xiaolian.amigo.data.manager.intf;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.support.annotation.NonNull;

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
public interface IBleDataManager {

    // 扫描设备
    Observable<ScanResult> scan();

    /**
     * 提供连接蓝牙设备的Observable，附带连接共享适配器供后续蓝牙操作使用
     *
     * @param macAddress  蓝牙设备mac地址
     * @param autoConnect 是否自动连接
     */
    Observable<RxBleConnection> prepareConnectionObservable(@NonNull String macAddress, boolean autoConnect);

    // 连接蓝牙
    Observable<BluetoothGattCharacteristic> connect(Observable<RxBleConnection> connectionObservable);

    /**
     * 向设备写特征值描述
     *
     * @param connectionObservable    连接句柄
     * @param bluetoothGattDescriptor 特征值描述
     */
    Observable<byte[]> writeDescriptor(@NonNull Observable<RxBleConnection> connectionObservable, @NonNull BluetoothGattDescriptor bluetoothGattDescriptor);

    /**
     * 向蓝牙设备发送数据
     *
     * @param connectionObservable 连接句柄
     * @param inputBytes           待发送的数据
     */
    Observable<byte[]> write(@NonNull Observable<RxBleConnection> connectionObservable, @NonNull byte[] inputBytes);

    /**
     * 接受蓝牙设备通知的数据
     *
     * @param connectionObservable 连接句柄
     */
    Observable<byte[]> notify(@NonNull Observable<RxBleConnection> connectionObservable);

    /**
     * 监控设备状态
     *
     * @param macAddress 蓝牙设备mac地址
     */
    Observable<RxBleConnection.RxBleConnectionState> monitorStatus(@NonNull String macAddress);

    /**
     * 获取设备当前状态
     *
     * @param macAddress 蓝牙设备mac地址
     */
    RxBleConnection.RxBleConnectionState getStatus(@NonNull String macAddress);
}