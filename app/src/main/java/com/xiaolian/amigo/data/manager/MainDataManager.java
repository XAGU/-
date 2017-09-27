package com.xiaolian.amigo.data.manager;

import com.xiaolian.amigo.data.manager.intf.IMainDataManager;
import com.xiaolian.amigo.data.network.IMainApi;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.QueryTimeValidReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.ReadNotifyReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.PersonalExtraInfoDTO;
import com.xiaolian.amigo.data.network.model.dto.response.QueryTimeValidRespDTO;
import com.xiaolian.amigo.data.network.model.user.User;
import com.xiaolian.amigo.data.prefs.ISharedPreferencesHelp;

import javax.inject.Inject;

import retrofit2.Retrofit;
import retrofit2.http.Body;
import rx.Observable;

/**
 * 主页
 * <p>
 * Created by zcd on 9/20/17.
 */

public class MainDataManager implements IMainDataManager {
    private static final String TAG = MainDataManager.class.getSimpleName();

    private ISharedPreferencesHelp sharedPreferencesHelp;
    private IMainApi mainApi;

    @Inject
    public MainDataManager(Retrofit retrofit, ISharedPreferencesHelp sharedPreferencesHelp) {
        this.sharedPreferencesHelp = sharedPreferencesHelp;
        this.mainApi = retrofit.create(IMainApi.class);
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

    @Override
    public Observable<ApiResult<PersonalExtraInfoDTO>> getExtraInfo() {
        return mainApi.getExtraInfo();
    }

    @Override
    public Observable<ApiResult<QueryTimeValidRespDTO>> queryWaterTimeValid(@Body QueryTimeValidReqDTO reqDTO) {
        return mainApi.queryWaterTimeValid(reqDTO);
    }

    @Override
    public Observable<ApiResult<BooleanRespDTO>> readUrgentNotify(@Body ReadNotifyReqDTO reqDTO) {
        return mainApi.readUrgentNotify(reqDTO);
    }
}
