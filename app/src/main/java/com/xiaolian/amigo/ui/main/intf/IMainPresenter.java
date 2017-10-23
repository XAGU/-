package com.xiaolian.amigo.ui.main.intf;

import com.xiaolian.amigo.data.network.model.user.User;
import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 主页
 * <p>
 * Created by zcd on 9/20/17.
 */

public interface IMainPresenter<V extends IMainView> extends IBasePresenter<V> {
    boolean isLogin();

    void logout();

    User getUserInfo();

    String getToken();

    void getNoticeAmount();

    void setBalance(String balance);

    String getBalance();

    void setBonusAmount(int bonusAmount);

    int getBonusAmount();

    void readUrgentNotify(Long id);

    boolean isShowUrgencyNotify();

    void setShowUrgencyNotify(boolean isShow);

    void gotoHeaterDevice(String defaultMacAddress, String location, Long residenceId);

    void getSchoolBusiness();

    void checkDeviceUsage(int type);
}
