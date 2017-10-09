package com.xiaolian.amigo.data.manager.intf;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.support.annotation.NonNull;

import com.polidea.rxandroidble.RxBleConnection;
import com.polidea.rxandroidble.scan.ScanResult;
import com.xiaolian.amigo.data.network.ITradeApi;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * 用水交易流程
 * <p>
 * Created by caidong on 2017/9/22.
 */
public interface ITradeDataManager extends ITradeApi {



}
