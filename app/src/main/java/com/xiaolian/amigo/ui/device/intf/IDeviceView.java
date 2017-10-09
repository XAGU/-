package com.xiaolian.amigo.ui.device.intf;


import com.xiaolian.amigo.ui.base.intf.IBaseView;

/**
 * Created by caidong on 2017/9/22.
 */
public interface IDeviceView extends IBaseView {

    // 连接设备失败
    void onConnectError();

    // 向设备发送数据失败
    void onWriteError();

    // 接受设备数据失败
    void onNotifyError();

    // 设备状态错误（通常为不在线）
    void onStatusError();

    // 设备开阀成功
    void onOpen();

    // 设备用水结束
    void onFinish();

    // 设备连接成功
    void onConnectSuccess();

    // 设备重连成功
    void onReconnectSuccess();
}
