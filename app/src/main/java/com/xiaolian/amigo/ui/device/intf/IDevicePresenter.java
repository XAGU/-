package com.xiaolian.amigo.ui.device.intf;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.xiaolian.amigo.data.enumeration.TradeStep;
import com.xiaolian.amigo.ui.base.intf.IBasePresenter;
import com.xiaolian.amigo.ui.base.intf.IBaseView;

/**
 * 设备账单
 *
 * @author caidong
 * @date 17/9/22
 */
public interface IDevicePresenter<V extends IBaseView> extends IBasePresenter<V> {

    /**
     * 连接前准备
     *
     * @param macAddress 设备mac地址
     */
    void onPreConnect(@NonNull String macAddress);

    /**
     * 扫一扫的连接设备前准备
     * @param macAddress
     * @param isScan
     */
    void onPreConnect(@NonNull String macAddress  , boolean isScan);

    /**
     * 连接设备
     *
     * @param macAddress 设备mac地址
     */
    void onConnect(@NonNull String macAddress);

    /**
     * 连接设备
     *
     * @param macAddress 设备mac地址
     * @param supplierId 供应商id
     */
    void onConnect(@NonNull String macAddress, Long supplierId);

    /**
     * 重新连接设备
     *
     * @param macAddress 设备mac地址
     */
    void onReconnect(@NonNull String macAddress);

    /**
     * 向设备下发指令
     *
     * @param command 指令
     */
    void onWrite(@NonNull String command);

    /**
     * 点击支付
     *
     * @param prepay  预付金额
     * @param bonusId 红包id
     */
    void onPay(@Nullable Double prepay, @Nullable Long bonusId);

    /**
     * 更新费率
     *
     * @param macAddress  设备地址
     */
    void onUpdateDeviceRate(@Nullable String macAddress);

    /**
     * 点击结束用水
     */
    void onClose();

    /**
     * 断开连接
     */
    void onDisConnect();

    /**
     * 设置当前页面操作步骤
     *
     * @param step 操作步骤
     */
    void setStep(TradeStep step);

    /**
     * 获取当前页面操作步骤
     *
     * @return 操作步骤
     */
    TradeStep getStep();

    /**
     * 重置蓝牙连接
     */
    void resetBleConnection();

    /**
     * 关闭蓝牙连接
     */
    void closeBleConnection();

    /**
     * 重置连接观察者管理器
     */
    void resetSubscriptions();

    /**
     * 重置上下文内容
     */
    void resetContext();

    /**
     * 取消连监控接定时器
     */
    void cancelTimer();

    /**
     * 标识是否从首页跳转
     *
     * @param homePageJump 是否是从首页跳转而来
     */
    void setHomePageJump(boolean homePageJump);

    /**
     * 设置供应商
     *
     * @param supplierId 供应商id
     */
    void setSupplierId(Long supplierId);

    void deleteLogFile();


}
