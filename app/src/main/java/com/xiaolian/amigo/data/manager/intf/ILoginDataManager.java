package com.xiaolian.amigo.data.manager.intf;

import com.xiaolian.amigo.data.network.ILoginApi;
import com.xiaolian.amigo.data.network.model.user.User;

/**
 * LoginDataManager接口
 * @author zcd
 */
public interface ILoginDataManager extends ILoginApi {
    String getToken();

    void setToken(String token);

    User getUserInfo();

    void setUserInfo(User user);

    void logout();
}
