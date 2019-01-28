package com.xiaolian.amigo.ui.device.intf;

/**
 * 用水设备 热水澡 饮水机
 *
 * @author zcd
 * @date 17/10/13
 */

public interface IWaterDeviceBasePresenter<V extends IWaterDeviceBaseView> extends IDevicePresenter<V> {
    /**
     * 设置红包金额
     *
     * @param amount 红包金额
     */
    void setBonusAmount(int amount);

    /**
     * 获取红包金额
     *
     * @return
     */
    int getBonusAmount();

    /**
     * 查询钱包
     *
     * @param amount 金额
     */
    void queryWallet(double amount);

    /**
     * 获取预付信息
     *
     * @param deviceType 设备类型
     */
    void queryPrepayOption(int deviceType);

    /**
     * 获取客服信息
     */
    void queryCsInfo();

    /**
     * 设置连接状态
     *
     * @param connecting 连接状态
     */
    void setConnecting(boolean connecting);

    /**
     * 显示温馨提示
     */
    void showGuide();

    String getAccessToken();

    String getRefreshToken();

    Long getSchoolId();
}
