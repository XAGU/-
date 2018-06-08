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

    @Override
    public Long getUserId() {
        return sharedPreferencesHelp.getUserInfo().getId();
    }

    @Override
    public void deletePushToken() {
        sharedPreferencesHelp.setPushToken("");
    }

    @Override
    public Long getSchoolId() {
        return sharedPreferencesHelp.getUserInfo().getSchoolId();
    }

    @Override
    public String getPushTag() {
        return sharedPreferencesHelp.getPushTag();
    }

    @Override
    public void setPushTag(String pushTag) {

    }
}
