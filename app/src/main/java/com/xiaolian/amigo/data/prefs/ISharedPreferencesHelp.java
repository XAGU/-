package com.xiaolian.amigo.data.prefs;

import com.xiaolian.amigo.data.network.model.user.User;

/**
 * SharedPreference帮助接口
 * @author zcd
 */

public interface ISharedPreferencesHelp {
    String getToken();

    void setToken(String token);

    User getUserInfo();

    void setUserInfo(User user);

    boolean isShowUrgencyNotify();

    void setShowUrgencyNotify(boolean isShow);

    void setBonusAmount(int amount);

    int getBonusAmount();

    void setBalance(String balance);

    String getBalance();

    void logout();

    // current http device token
    void setCurrentDeviceToken(String deviceToken);

    String getCurrentDeviceToken();

    // http device token
    void setDeviceToken(String macAddress, String deviceToken);

    String getDeviceToken(String macAddress);

    // 握手指令
    void setConnectCmd(String macAddress, String connectCmd);

    String getConnectCmd(String macAddress);

    // 关阀指令
    void setCloseCmd(String macAddress, String closeCmd);

    String getCloseCmd(String macAddress);

    // 上次连接时间
    void setLastConnectTime(Long lastConnectTime);

    Long getLastConnectTime();
}
