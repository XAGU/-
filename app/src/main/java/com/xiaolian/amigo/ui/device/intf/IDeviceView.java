package com.xiaolian.amigo.ui.device.intf;


import com.xiaolian.amigo.data.enumeration.TradeError;
import com.xiaolian.amigo.data.enumeration.TradeStep;
import com.xiaolian.amigo.ui.base.intf.IBaseView;

/**
 * 设备View
 *
 * @author caidong
 * @date 17/9/22
 */
public interface IDeviceView extends IBaseView {

    /**
     * 设备操作失败
     *
     * @param tradeError 错误类型
     */
    void onError(TradeError tradeError);

    /**
     * 连接设备失败
     */
    void onConnectError();

    /**
     * 向设备发送数据失败
     */
    void onWriteError();

    /**
     * 接受设备数据失败
     */
    void onNotifyError();

    /**
     * 设备状态错误（通常为不在线）
     */
    void onStatusError();

    /**
     * 预付进行使用
     */
    void realPay();

    /**
     * 设备开阀成功
     */
    void onOpen();

    /**
     * 设备用水结束
     *
     * @param orderId 订单id
     */
    void onFinish(long orderId);

    /**
     * 设备连接成功
     *
     * @param step  跳转至第几步
     * @param extra 其他参数
     */
    void onConnectSuccess(TradeStep step, Object... extra);

    /**
     * 重连成功
     *
     * @param extra 其他参数
     */
    void onReconnectSuccess(Object... extra);

}
