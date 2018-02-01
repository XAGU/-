package com.xiaolian.amigo.data.manager;

import com.xiaolian.amigo.data.manager.intf.IMoreDataManager;
import com.xiaolian.amigo.data.prefs.ISharedPreferencesHelp;

import javax.inject.Inject;

/**
 * 更多
 *
 * @author zcd
 * @date 17/10/13
 */

public class MoreDataManager implements IMoreDataManager {

    private ISharedPreferencesHelp sharedPreferencesHelp;

    @Inject
    public MoreDataManager(ISharedPreferencesHelp sharedPreferencesHelp) {
        this.sharedPreferencesHelp = sharedPreferencesHelp;
    }

    @Override
    public void logout() {
        sharedPreferencesHelp.logout();
    }

    @Override
    public String getToken() {
        return sharedPreferencesHelp.getToken();
    }

    @Override
    public boolean getTransfer() {
        return sharedPreferencesHelp.getTransfer();
    }
}
