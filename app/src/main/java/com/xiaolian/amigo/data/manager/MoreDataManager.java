package com.xiaolian.amigo.data.manager;

import com.xiaolian.amigo.data.manager.intf.IMoreDataManager;
import com.xiaolian.amigo.data.network.IDeviceConnectErrorApi;
import com.xiaolian.amigo.data.network.ITokenApi;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.prefs.ISharedPreferencesHelp;
import com.xiaolian.amigo.di.UserServer;

import javax.inject.Inject;

import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;

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

    @Override
    public String getAccessToken() {
        return sharedPreferencesHelp.getAccessToken();
    }

    @Override
    public String getRefreshToken() {
        return sharedPreferencesHelp.getReferToken();
    }

    @Override
    public String getMobile() {
        return sharedPreferencesHelp.getUserInfo().getMobile();
    }

}
