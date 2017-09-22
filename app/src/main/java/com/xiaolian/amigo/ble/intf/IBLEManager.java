package com.xiaolian.amigo.ble.intf;

import com.xiaolian.amigo.ble.BLEDevice;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * 蓝牙连接操作
 * <p>
 * Created by caidong on 2017/9/22.
 */
public interface IBLEManager {

    // 校验蓝牙是否打开
    void check(Consumer<IBLEManager> consumer);

    // 打开蓝牙
    void open();

    /**
     * 搜索蓝牙设备
     *
     * @param millis 搜索超时时间
     */
    Observable<BLEDevice> search(long millis);
}
