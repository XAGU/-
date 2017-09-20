package com.xiaolian.amigo.data.manager;

import com.xiaolian.amigo.data.manager.intf.IMainDataManager;
import com.xiaolian.amigo.data.network.model.user.User;
import com.xiaolian.amigo.data.prefs.ISharedPreferencesHelp;

import javax.inject.Inject;

/**
 * 主页
 * <p>
 * Created by zcd on 9/20/17.
 */

public class MainDataManager implements IMainDataManager {
    private static final String TAG = MainDataManager.class.getSimpleName();

    private ISharedPreferencesHelp sharedPreferencesHelp;

    @Inject
    public MainDataManager(ISharedPreferencesHelp sharedPreferencesHelp) {
        this.sharedPreferencesHelp = sharedPreferencesHelp;
    }

    @Override
    public String getToken() {
        return sharedPreferencesHelp.getToken();
    }

    @Override
    public void setToken(String token) {
        sharedPreferencesHelp.setToken(token);
    }

    @Override
    public User getUserInfo() {
        return sharedPreferencesHelp.getUserInfo();
    }

    @Override
    public void setUserInfo(User user) {
        sharedPreferencesHelp.setUserInfo(user);
    }
}
