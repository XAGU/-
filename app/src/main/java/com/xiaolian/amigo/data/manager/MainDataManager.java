package com.xiaolian.amigo.data.manager;

import com.xiaolian.amigo.data.manager.intf.IMainDataManager;
import com.xiaolian.amigo.data.network.IMainApi;
import com.xiaolian.amigo.data.network.IOrderApi;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.DeviceCheckReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.OrderReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.QueryDeviceListReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.QueryTimeValidReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.ReadNotifyReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.DeviceCheckRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.OrderRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.PersonalExtraInfoDTO;
import com.xiaolian.amigo.data.network.model.dto.response.QueryDeviceListRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.QuerySchoolBizListRespDTO;
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
    private IOrderApi orderApi;

    @Inject
    public MainDataManager(Retrofit retrofit, ISharedPreferencesHelp sharedPreferencesHelp) {
        this.sharedPreferencesHelp = sharedPreferencesHelp;
        this.mainApi = retrofit.create(IMainApi.class);
        this.orderApi = retrofit.create(IOrderApi.class);
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
    public void logout() {
        sharedPreferencesHelp.logout();
    }

    @Override
    public boolean isShowUrgencyNotify() {
        return sharedPreferencesHelp.isShowUrgencyNotify();
    }

    @Override
    public void setShowUrgencyNotify(boolean isShow) {
        sharedPreferencesHelp.setShowUrgencyNotify(isShow);
    }

    @Override
    public void setBonusAmount(int amount) {
        sharedPreferencesHelp.setBonusAmount(amount);
    }

    @Override
    public int getBonusAmount() {
        return sharedPreferencesHelp.getBonusAmount();
    }

    @Override
    public void setBalance(String balance) {
        sharedPreferencesHelp.setBalance(balance);
    }

    @Override
    public String getBalance() {
        return sharedPreferencesHelp.getBalance();
    }

    @Override
    public Observable<ApiResult<OrderRespDTO>> queryOrders(@Body OrderReqDTO reqDTO) {
        return orderApi.queryOrders(reqDTO);
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
    public Observable<ApiResult<DeviceCheckRespDTO>> checkDeviceUseage(@Body DeviceCheckReqDTO reqDTO) {
        return mainApi.checkDeviceUseage(reqDTO);
    }

    @Override
    public Observable<ApiResult<BooleanRespDTO>> readUrgentNotify(@Body ReadNotifyReqDTO reqDTO) {
        return mainApi.readUrgentNotify(reqDTO);
    }

    @Override
    public Observable<ApiResult<QuerySchoolBizListRespDTO>> getSchoolBizList() {
        return mainApi.getSchoolBizList();
    }
}
