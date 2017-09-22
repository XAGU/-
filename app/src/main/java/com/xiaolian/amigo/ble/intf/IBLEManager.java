package com.xiaolian.amigo.ble.intf;

import io.reactivex.functions.Consumer;

/**
 * 蓝牙连接操作
 * <p>
 * Created by caidong on 2017/9/22.
 */
public interface IBLEManager {

    // 校验蓝牙是否打开
    void check(Consumer<IBLEManager> consumer);


}
