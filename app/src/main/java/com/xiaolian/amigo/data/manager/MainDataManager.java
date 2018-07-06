package com.xiaolian.amigo.data.manager;

import com.xiaolian.amigo.data.manager.intf.IMainDataManager;
import com.xiaolian.amigo.data.network.IDeviceApi;
import com.xiaolian.amigo.data.network.INotifyApi;
import com.xiaolian.amigo.data.network.IOrderApi;
import com.xiaolian.amigo.data.network.ISchoolApi;
import com.xiaolian.amigo.data.network.ISystemApi;
import com.xiaolian.amigo.data.network.ITimeRangeApi;
import com.xiaolian.amigo.data.network.IUserApi;
import com.xiaolian.amigo.data.network.IVersionApi;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.device.DeviceCategoryBO;
import com.xiaolian.amigo.data.network.model.user.BriefSchoolBusiness;
import com.xiaolian.amigo.data.network.model.version.CheckVersionUpdateReqDTO;
import com.xiaolian.amigo.data.network.model.device.DeviceCheckReqDTO;
import com.xiaolian.amigo.data.network.model.order.OrderReqDTO;
import com.xiaolian.amigo.data.network.model.timerange.QueryTimeValidReqDTO;
import com.xiaolian.amigo.data.network.model.notify.ReadNotifyReqDTO;
import com.xiaolian.amigo.data.network.model.system.BaseInfoDTO;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.version.CheckVersionUpdateRespDTO;
import com.xiaolian.amigo.data.network.model.device.DeviceCheckRespDTO;
import com.xiaolian.amigo.data.network.model.order.OrderRespDTO;
import com.xiaolian.amigo.data.network.model.user.PersonalExtraInfoDTO;
import com.xiaolian.amigo.data.network.model.school.QuerySchoolBizListRespDTO;
import com.xiaolian.amigo.data.network.model.timerange.QueryTimeValidRespDTO;
import com.xiaolian.amigo.data.network.model.version.VersionDTO;
import com.xiaolian.amigo.data.network.model.user.UploadUserDeviceInfoReqDTO;
import com.xiaolian.amigo.data.vo.DeviceCategory;
import com.xiaolian.amigo.data.vo.User;
import com.xiaolian.amigo.data.prefs.ISharedPreferencesHelp;
import com.xiaolian.amigo.di.UserServer;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Retrofit;
import retrofit2.http.Body;
import rx.Observable;

/**
 * 主页
 *
 * @author zcd
 * @date 17/9/20
 */

public class MainDataManager implements IMainDataManager {
    private static final String TAG = MainDataManager.class.getSimpleName();

    private ISharedPreferencesHelp sharedPreferencesHelp;
    private IOrderApi orderApi;
    private IUserApi userApi;
    private ISystemApi systemApi;
    private ITimeRangeApi timeRangeApi;
    private IDeviceApi deviceApi;
    private INotifyApi notifyApi;
    private ISchoolApi schoolApi;
    private IVersionApi versionApi;

    @Inject
    public MainDataManager(@UserServer Retrofit retrofit, ISharedPreferencesHelp sharedPreferencesHelp) {
        this.sharedPreferencesHelp = sharedPreferencesHelp;
        this.orderApi = retrofit.create(IOrderApi.class);
        this.systemApi = retrofit.create(ISystemApi.class);
        this.userApi = retrofit.create(IUserApi.class);
        this.timeRangeApi = retrofit.create(ITimeRangeApi.class);
        this.deviceApi = retrofit.create(IDeviceApi.class);
        this.notifyApi = retrofit.create(INotifyApi.class);
        this.schoolApi = retrofit.create(ISchoolApi.class);
        this.versionApi = retrofit.create(IVersionApi.class);
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
    public Observable<ApiResult<BaseInfoDTO>> getSystemBaseInfo() {
        return systemApi.getSystemBaseInfo();
    }

    @Override
    public void setLastUpdateRemindTime() {
        sharedPreferencesHelp.setLastUpdateRemindTime();
    }

    @Override
    public Long getLastUpdateRemindTime() {
        return sharedPreferencesHelp.getLastUpdateRemindTime();
    }

    @Override
    public Integer getMainGuide() {
        return sharedPreferencesHelp.getMainGuide();
    }

    @Override
    public void setMainGuide(Integer guideTime) {
        sharedPreferencesHelp.setMainGuide(guideTime);
    }

    @Override
    public void setLastRepairTime(Long time) {
        sharedPreferencesHelp.setLastRepairTime(time);
    }

    @Override
    public Long getLastRepairTime() {
        return sharedPreferencesHelp.getLastRepairTime();
    }

    @Override
    public Observable<ApiResult<BooleanRespDTO>> uploadDeviceInfo(UploadUserDeviceInfoReqDTO reqDTO) {
        return userApi.uploadDeviceInfo(reqDTO);
    }

    @Override
    public void saveUploadedUserDeviceInfo(UploadUserDeviceInfoReqDTO reqDTO) {
        sharedPreferencesHelp.saveUploadedUserDeviceInfo(reqDTO);
    }

    @Override
    public UploadUserDeviceInfoReqDTO getUploadedUserDeviceInfo() {
        return sharedPreferencesHelp.getUploadedUserDeviceInfo();
    }

    @Override
    public Observable<ApiResult<PersonalExtraInfoDTO>> getExtraInfo() {
        return userApi.getUserExtraInfo();
    }

    @Override
    public Observable<ApiResult<QueryTimeValidRespDTO>> queryWaterTimeValid(@Body QueryTimeValidReqDTO reqDTO) {
        return timeRangeApi.queryWaterTimeValid(reqDTO);
    }

    @Override
    public Observable<ApiResult<DeviceCheckRespDTO>> checkDeviceUseage(@Body DeviceCheckReqDTO reqDTO) {
        return deviceApi.checkDeviceUseage(reqDTO);
    }

    @Override
    public Observable<ApiResult<BooleanRespDTO>> readUrgentNotify(@Body ReadNotifyReqDTO reqDTO) {
        return notifyApi.readUrgentNotify(reqDTO);
    }

    @Override
    public Observable<ApiResult<QuerySchoolBizListRespDTO>> getSchoolBizList() {
        return schoolApi.getSchoolBizList();
    }

    @Override
    public Observable<ApiResult<CheckVersionUpdateRespDTO>> checkUpdate(CheckVersionUpdateReqDTO reqDTO) {
        return systemApi.checkUpdate(reqDTO);
    }

    @Override
    public Observable<ApiResult<VersionDTO>> getUpdateInfo() {
        return versionApi.getUpdateInfo();
    }

    @Override
    public void saveDeviceCategory(List<DeviceCategoryBO> devices) {
        if (devices == null) {
            return;
        }
        List<DeviceCategory> deviceCategories = new ArrayList<>();
        for (DeviceCategoryBO bo : devices) {
            deviceCategories.add(bo.transform());
        }
        sharedPreferencesHelp.saveDeviceCategory(deviceCategories);
    }

    @Override
    public void setNeedTransfer() {
        sharedPreferencesHelp.setTransfer(true);
    }

    @Override
    public void setNotNeedTransfer() {
        sharedPreferencesHelp.setTransfer(false);
    }

    @Override
    public void setCredits(Integer credits) {
        sharedPreferencesHelp.setCredits(credits);
    }

    @Override
    public Integer getCredits() {
        return sharedPreferencesHelp.getCredits();
    }

    @Override
    public void setSchoolBiz(List<BriefSchoolBusiness> businesses) {
        sharedPreferencesHelp.setSchoolBiz(businesses);
    }

    @Override
    public List<BriefSchoolBusiness> getSchoolBiz() {
        return sharedPreferencesHelp.getSchoolBiz();
    }

    @Override
    public void deletePushToken() {
        sharedPreferencesHelp.setPushToken("");
    }

    @Override
    public String getPushToken() {
        return sharedPreferencesHelp.getPushToken();
    }

    @Override
    public void setPushToken(String pushToken) {
        sharedPreferencesHelp.setPushToken(pushToken);
    }
}
