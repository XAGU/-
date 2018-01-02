package com.xiaolian.amigo.ui.device.intf.dispenser;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 选择饮水机
 * <p>
 * Created by zcd on 10/13/17.
 */

public interface IChooseDispenserPresenter<V extends IChooseDispenerView> extends IBasePresenter<V> {
    void requestFavorites();

    // 页面加载时触发
    void onLoad();

    // 关闭蓝牙连接
    void closeBleConnection();

    void setListStatus(boolean listStatus);

    void setAction(int action);

    int getAction();

    void finishView();

    void gotoDispenser(String macAddress, boolean favor, Long residenceId, String usefor, String location);

    void startTimer();
    void cancelTimer();
}
