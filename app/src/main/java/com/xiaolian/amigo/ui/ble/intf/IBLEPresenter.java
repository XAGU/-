package com.xiaolian.amigo.ui.ble.intf;

import android.support.annotation.NonNull;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;
import com.xiaolian.amigo.ui.order.intf.IOrderView;

/**
 * Created by caidong on 2017/9/22.
 */
public interface IBLEPresenter<V extends IBLEView> extends IBasePresenter<V> {

    // 扫描设备
    void onScan();

    // 连接设备
    void onConnect(@NonNull String macAddress);

    // 向设备下发指令
    void onWrite(@NonNull String command);

    // 接收设备通知（读数据）
    void registerNotify();
}
