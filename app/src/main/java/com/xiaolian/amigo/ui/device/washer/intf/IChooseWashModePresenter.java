package com.xiaolian.amigo.ui.device.washer.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 选择洗衣机模式
 * <p>
 * Created by zcd on 18/1/12.
 */

public interface IChooseWashModePresenter<V extends IChooseWashModeView> extends IBasePresenter<V> {

    String getDeviceNo();

    void setDeviceNo(String deviceNo);

    void getWasherMode();

    void payAndGenerate(Long bonusId, String modeDesc, String deviceNo, String price, Integer mode);
}
