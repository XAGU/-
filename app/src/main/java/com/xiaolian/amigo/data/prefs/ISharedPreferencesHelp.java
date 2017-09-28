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

    void logout();
}
