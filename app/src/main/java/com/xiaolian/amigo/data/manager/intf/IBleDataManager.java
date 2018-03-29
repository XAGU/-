package com.xiaolian.amigo.data.manager.intf;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.support.annotation.NonNull;

import com.polidea.rxandroidble.RxBleConnection;
import com.polidea.rxandroidble.scan.ScanResult;
import com.xiaolian.blelib.connect.BluetoothCharacteristicNotifyCallback;
import com.xiaolian.blelib.connect.BluetoothConnectCallback;
import com.xiaolian.blelib.connect.BluetoothConnectStatusListener;
import com.xiaolian.blelib.connect.BluetoothWriteCharacteristicCallback;
import com.xiaolian.blelib.connect.BluetoothWriteDescriptorCallback;
import com.xiaolian.blelib.scan.BluetoothScanResponse;

import java.util.UUID;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * 蓝牙相关操作
 *
 * @author caidong
 * @date 17/9/22
 */
public interface IBleDataManager {

    /**
     * 扫描设备
     */
    Observable<ScanResult> scan();

    /**
     * 扫描指定名称的设备
     */
    Observable<ScanResult> scan(String deviceName);

    void scan(int scanType, BluetoothScanResponse response);

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

    /**
     * 提供连接蓝牙设备的Observable，附带连接共享适配器供后续蓝牙操作使用
     *
     * @param macAddress               蓝牙设备mac地址
     * @param autoConnect              是否自动连接
     * @param disconnectTriggerSubject 断连触发器
     */
    Observable<RxBleConnection> prepareConnectionObservable(@NonNull String macAddress, boolean autoConnect, @NonNull PublishSubject<Void> disconnectTriggerSubject);

    /**
     * 好年华连接蓝牙
     */
    Observable<BluetoothGattCharacteristic> connect(Observable<RxBleConnection> connectionObservable);

    /**
     * 辛纳连接蓝牙
     */
    Observable<Observable<byte[]>> connect2(Observable<RxBleConnection> connectionObservable, String notifyCharacteristicUuid);

    /**
     * 设置notify通道模式为enable
     */
    Observable<Observable<byte[]>> setupNotification(Observable<RxBleConnection> connectionObservable, BluetoothGattCharacteristic characteristic);

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
    Observable<byte[]> write(@NonNull Observable<RxBleConnection> connectionObservable, @NonNull byte[] inputBytes, String writeCharacteristicUuid);

    /**
     * 接受蓝牙设备通知的数据
     *
     * @param connectionObservable 连接句柄
     */
    Observable<byte[]> notify(@NonNull Observable<RxBleConnection> connectionObservable, BluetoothGattCharacteristic characteristic);

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
