package com.xiaolian.amigo.ui.ble.intf;

import android.support.annotation.NonNull;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * Created by caidong on 2017/9/22.
 */
public interface IBleReactivePresenter<V extends IBleInteractiveView> extends IBasePresenter<V> {

    // 连接设备
    void onConnect(@NonNull String macAddress);

    // 向设备下发指令
    void onWrite(@NonNull String command);

    // 接收设备通知（读数据）
    void registerNotify();
}
