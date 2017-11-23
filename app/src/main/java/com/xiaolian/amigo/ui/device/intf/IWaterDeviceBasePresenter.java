package com.xiaolian.amigo.ui.device.intf;

/**
 * 用水设备 热水澡 饮水机
 * <p>
 * Created by zcd on 10/13/17.
 */

public interface IWaterDeviceBasePresenter<V extends IWaterDeviceBaseView> extends IDevicePresenter<V> {
    void setBonusAmount(int amount);

    int getBonusAmount();

    void queryWallet(double amount);

    void queryPrepayOption(int deviceType);

    void queryCsInfo();

    void setConnecting(boolean connecting);
}
