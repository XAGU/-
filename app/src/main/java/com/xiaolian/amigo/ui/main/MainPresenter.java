package com.xiaolian.amigo.ui.main;

import android.text.TextUtils;

import com.xiaolian.amigo.data.enumeration.Device;
import com.xiaolian.amigo.data.manager.intf.IMainDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.CheckVersionUpdateReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.DeviceCheckReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.ReadNotifyReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.BannerDTO;
import com.xiaolian.amigo.data.network.model.dto.response.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.CheckVersionUpdateRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.DeviceCheckRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.PersonalExtraInfoDTO;
import com.xiaolian.amigo.data.network.model.dto.response.QuerySchoolBizListRespDTO;
import com.xiaolian.amigo.data.network.model.user.UploadUserDeviceInfoReqDTO;
import com.xiaolian.amigo.data.network.model.user.User;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.main.intf.IMainPresenter;
import com.xiaolian.amigo.ui.main.intf.IMainView;
import com.xiaolian.amigo.util.Constant;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

/**
 * 主页
 * <p>
 * Created by zcd on 9/20/17.
 */

public class MainPresenter<V extends IMainView> extends BasePresenter<V>
        implements IMainPresenter<V> {
    private static final String TAG = MainPresenter.class.getSimpleName();
    private IMainDataManager mainDataManager;
    private Integer guideTime;

    @Inject
    public MainPresenter(IMainDataManager mainDataManager) {
        this.mainDataManager = mainDataManager;
    }

    @Override
    public boolean isLogin() {
        return !TextUtils.isEmpty(mainDataManager.getToken());
    }

    @Override
    public void logout() {
        mainDataManager.logout();
    }

    @Override
    public User getUserInfo() {
        return mainDataManager.getUserInfo();
    }

    @Override
    public String getToken() {
        return mainDataManager.getToken();
    }

    @Override
    public void getNoticeAmount() {
        addObserver(mainDataManager.getExtraInfo(), new NetworkObserver<ApiResult<PersonalExtraInfoDTO>>(false) {

            @Override
            public void onReady(ApiResult<PersonalExtraInfoDTO> result) {
                if (null == result.getError()) {
                    if (!TextUtils.isEmpty(result.getData().getPreFileUrl())) {
                        Constant.IMAGE_PREFIX = result.getData().getPreFileUrl();
                    }
                    if (result.getData().getBanners() != null && result.getData().getBanners().size() > 0) {
                        for (BannerDTO banner : result.getData().getBanners()) {
                            banner.setLink(banner.getLink() + "?token="
                                    + mainDataManager.getToken());
                        }
                        getMvpView().showBanners(result.getData().getBanners());
                    }
                    if (result.getData().getUrgentNotify() != null) {
                        if (isShowUrgencyNotify()) {
                            getMvpView().showUrgentNotify(result.getData().getUrgentNotify().getContent(),
                                    result.getData().getUrgentNotify().getId());
                            setShowUrgencyNotify(false);
                        }
                    }
                    if (result.getData().getBonusAmount() != null) {
                        mainDataManager.setBonusAmount(result.getData().getBonusAmount());
                    }
                    PersonalExtraInfoDTO dto = result.getData();
                    if (dto.getLastRepairTime() != null && mainDataManager.getLastRepairTime()  < dto.getLastRepairTime()) {
                        dto.setNeedShowDot(true);
                    } else {
                        dto.setNeedShowDot(false);
                        dto.setLastRepairTime(null);
                    }
                    getMvpView().refreshProfile(dto);
                    getMvpView().showNoticeAmount(result.getData().getNotifyAmount());
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }

    @Override
    public void setBalance(String balance) {
        mainDataManager.setBalance(balance);
    }

    @Override
    public String getBalance() {
        return mainDataManager.getBalance();
    }

    @Override
    public void setBonusAmount(int bonusAmount) {
        mainDataManager.setBonusAmount(bonusAmount);
    }

    @Override
    public int getBonusAmount() {
        return mainDataManager.getBonusAmount();
    }

    @Override
    public void readUrgentNotify(Long id) {
        if (TextUtils.isEmpty(mainDataManager.getToken())) {
            return;
        }
        ReadNotifyReqDTO reqDTO = new ReadNotifyReqDTO();
        reqDTO.setId(id);
        addObserver(mainDataManager.readUrgentNotify(reqDTO), new NetworkObserver<ApiResult<BooleanRespDTO>>(false) {

            @Override
            public void onReady(ApiResult<BooleanRespDTO> result) {
                if (null == result.getError()) {
                    if (result.getData().isResult()) {
                        getMvpView().refreshNoticeAmount();
                    }
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public boolean isShowUrgencyNotify() {
        return mainDataManager.isShowUrgencyNotify();
    }

    @Override
    public void setShowUrgencyNotify(boolean isShow) {
        mainDataManager.setShowUrgencyNotify(isShow);
    }

    @Override
    public void gotoHeaterDevice(String defaultAddress, String location, Long residenceId) {
        if (TextUtils.isEmpty(defaultAddress)) {
            if (mainDataManager.getUserInfo().getResidenceId() == null) {
                getMvpView().showBindDormitoryDialog();
            } else {
                getMvpView().showNoDeviceDialog();
            }
        } else {
            getMvpView().gotoDevice(Device.HEATER, defaultAddress,
                    location, residenceId, false);
        }
    }

    @Override
    public boolean checkDefaultDormitoryExist() {
        return mainDataManager.getUserInfo().getResidenceId() != null;
    }

    @Override
    public void getSchoolBusiness() {
        addObserver(mainDataManager.getSchoolBizList(), new NetworkObserver<ApiResult<QuerySchoolBizListRespDTO>>(false) {

            @Override
            public void onReady(ApiResult<QuerySchoolBizListRespDTO> result) {
                if (null == result.getError()) {
                    getMvpView().showSchoolBiz(result.getData().getBusinesses());
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                    getMvpView().showSchoolBiz(null);
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                getMvpView().showSchoolBiz(null);
            }
        });
    }

    @Override
    public void checkDeviceUsage(int type) {
        DeviceCheckReqDTO reqDTO = new DeviceCheckReqDTO();
        reqDTO.setDeviceType(type);
        addObserver(mainDataManager.checkDeviceUseage(reqDTO), new NetworkObserver<ApiResult<DeviceCheckRespDTO>>() {

            @Override
            public void onReady(ApiResult<DeviceCheckRespDTO> result) {
                EventBus.getDefault().post(new HomeFragment2.Event(HomeFragment2.Event.EventType.ENABLE_VIEW));
                if (null == result.getError()) {
                    getMvpView().showDeviceUsageDialog(type, result.getData());
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                EventBus.getDefault().post(new HomeFragment2.Event(HomeFragment2.Event.EventType.ENABLE_VIEW));
            }
        });
    }

    @Override
    public void checkUpdate(Integer code, String versionNo) {
        CheckVersionUpdateReqDTO reqDTO = new CheckVersionUpdateReqDTO();
        reqDTO.setCode(code);
        reqDTO.setVersionNo(versionNo);
        addObserver(mainDataManager.checkUpdate(reqDTO),
                new NetworkObserver<ApiResult<CheckVersionUpdateRespDTO>>(false) {

            @Override
            public void onReady(ApiResult<CheckVersionUpdateRespDTO> result) {
                if (null == result.getError()) {
                    if (result.getData().getResult()) {
                        if (result.getData().getVersion().isMustUpdate()) {
                            getMvpView().showUpdateDialog(result.getData().getVersion());
                        } else {
                            // 小于6小时不再提醒
                            if (System.currentTimeMillis() - mainDataManager.getLastUpdateRemindTime() >= 6 * 1000 * 60 * 60) {
                                getMvpView().showUpdateDialog(result.getData().getVersion());
                            }
                        }
                        mainDataManager.setLastUpdateRemindTime();
                    }
                }
            }
        });
    }

    @Override
    public boolean isMainGuideDone() {
        if (guideTime == null) {
            guideTime = mainDataManager.getMainGuide();
            if (guideTime < 3) {
                guideTime ++;
                mainDataManager.setMainGuide(guideTime);
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    @Override
    public void setLastRepairTime(Long time) {
        mainDataManager.setLastRepairTime(time);
    }

    @Override
    public Long getLastRepairTime() {
        return mainDataManager.getLastRepairTime();
    }

    @Override
    public void uploadDeviceInfo(String appVersion, String brand, String model, int systemVersion, String androidId) {
        UploadUserDeviceInfoReqDTO reqDTO = new UploadUserDeviceInfoReqDTO();
        reqDTO.setAppVersion(appVersion);
        reqDTO.setBrand(brand);
        reqDTO.setModel(model);
        reqDTO.setSystem(UploadUserDeviceInfoReqDTO.SYSTEM_CODE);
        reqDTO.setUniqueId(androidId);
        reqDTO.setSystemVersion(String.valueOf(systemVersion));
        if (isDeviceInfoUploaded(mainDataManager.getUploadedUserDeviceInfo(), reqDTO)) {
            return;
        }
        addObserver(mainDataManager.uploadDeviceInfo(reqDTO), new NetworkObserver<ApiResult<BooleanRespDTO>>() {

            @Override
            public void onReady(ApiResult<BooleanRespDTO> result) {
                if (null == result.getError()) {
                    if (result.getData().isResult()) {
                        mainDataManager.saveUploadedUserDeviceInfo(reqDTO);
                    }
                }
            }
        });
    }

    private boolean isDeviceInfoUploaded(UploadUserDeviceInfoReqDTO newReq,
                                         UploadUserDeviceInfoReqDTO oldReq) {
        if (oldReq == null) {
            return false;
        }
        if (newReq == null) {
            return false;
        }
        if (!TextUtils.equals(newReq.getAppVersion(), oldReq.getAppVersion())) {
            return false;
        }
        if (!TextUtils.equals(newReq.getSystemVersion(), oldReq.getSystemVersion())) {
            return false;
        }
        if (!TextUtils.equals(newReq.getModel(), oldReq.getModel())) {
            return false;
        }
        if (!TextUtils.equals(newReq.getBrand(), oldReq.getBrand())) {
            return false;
        }
        if (!TextUtils.equals(newReq.getUniqueId(), oldReq.getUniqueId())) {
            return false;
        }
        return true;
    }

}
