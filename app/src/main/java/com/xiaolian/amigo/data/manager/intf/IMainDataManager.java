package com.xiaolian.amigo.data.manager.intf;

import com.xiaolian.amigo.data.network.IMainApi;
import com.xiaolian.amigo.data.network.model.user.User;

/**
 * 主页
 * <p>
 * Created by zcd on 9/20/17.
 */

public interface IMainDataManager extends IMainApi {

    String getToken();

    void setToken(String token);

    User getUserInfo();

    void setUserInfo(User user);

    void logout();

    boolean isShowUrgencyNotify();

    void setShowUrgencyNotify(boolean isShow);
}
