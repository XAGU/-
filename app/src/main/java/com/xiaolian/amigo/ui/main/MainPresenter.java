package com.xiaolian.amigo.ui.main;

import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import com.xiaolian.amigo.data.base.LogInterceptor;
import com.xiaolian.amigo.data.enumeration.Device;
import com.xiaolian.amigo.data.manager.intf.IMainDataManager;
import com.xiaolian.amigo.data.manager.intf.IUserDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.bathroom.BathRouteRespDTO;
import com.xiaolian.amigo.data.network.model.bathroom.CurrentBathOrderRespDTO;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.device.DeviceCheckReqDTO;
import com.xiaolian.amigo.data.network.model.device.DeviceCheckRespDTO;
import com.xiaolian.amigo.data.network.model.lostandfound.NoticeCountDTO;
import com.xiaolian.amigo.data.network.model.notify.ReadNotifyReqDTO;
import com.xiaolian.amigo.data.network.model.notify.RollingNotifyRespDTO;
import com.xiaolian.amigo.data.network.model.school.QuerySchoolBizListRespDTO;
import com.xiaolian.amigo.data.network.model.school.SchoolForumStatusDTO;
import com.xiaolian.amigo.data.network.model.system.BannerDTO;
import com.xiaolian.amigo.data.network.model.user.BriefSchoolBusiness;
import com.xiaolian.amigo.data.network.model.user.PersonalExtraInfoDTO;
import com.xiaolian.amigo.data.network.model.user.UploadUserDeviceInfoReqDTO;
import com.xiaolian.amigo.data.network.model.version.CheckVersionUpdateReqDTO;
import com.xiaolian.amigo.data.network.model.version.CheckVersionUpdateRespDTO;
import com.xiaolian.amigo.data.network.model.version.VersionDialogTime;
import com.xiaolian.amigo.data.vo.User;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.main.intf.IMainPresenter;
import com.xiaolian.amigo.ui.main.intf.IMainView;
import com.xiaolian.amigo.util.CommonUtil;
import com.xiaolian.amigo.util.Constant;
import com.xiaolian.amigo.util.FileUtils;
import com.xiaolian.amigo.util.Log;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import rx.schedulers.Schedulers;

/**
 * 主页
 *
 * @author zcd
 * @date 17/9/20
 */

public class MainPresenter<V extends IMainView> extends BasePresenter<V>
        implements IMainPresenter<V>  {

    private static final String DeviceLogFileName = "DeviceLog.txt";
    private static final String TAG = MainPresenter.class.getSimpleName();
    private IMainDataManager mainDataManager;
    private IUserDataManager userDataManager;
    private LogInterceptor interceptor;

    int noticeCount;

    private boolean isShowRepair = false;

    private BathRouteRespDTO bathRouteRespDTO ;


    @Inject
    MainPresenter(IMainDataManager mainDataManager, IUserDataManager userDataManager, LogInterceptor interceptor) {
        this.mainDataManager = mainDataManager;
        this.interceptor = interceptor;
        this.userDataManager = userDataManager;
    }

    @Override
    public void onAttach(V view) {
        super.onAttach(view);
        setUpInterceptor();
    }


    private void setBathRouteRespDTO(BathRouteRespDTO bathRouteRespDTO){
        this.bathRouteRespDTO = bathRouteRespDTO ;
    }

    public BathRouteRespDTO getBathRouteRespDTO() {
        return bathRouteRespDTO;
    }

    private void setUpInterceptor() {
        String androidId = getMvpView().getAndroidId();
        String model = Build.MODEL;
        String brand = Build.BRAND;
        int systemVersion = Build.VERSION.SDK_INT;
        String appVersion = getMvpView().getAppVersion();
        interceptor.setAppVersion(appVersion);
        interceptor.setUniqueId(androidId);
        interceptor.setModel(model);
        interceptor.setBrand(brand);
        interceptor.setSystemVersion(String.valueOf(systemVersion));
    }


    @Override
    public String getAccessToken() {
        return mainDataManager.getAccessToken();
    }

    @Override
    public String getRefreshToken() {
        return mainDataManager.getRefreshToken();
    }

    @Override
    public boolean isLogin() {
        return !TextUtils.isEmpty(mainDataManager.getAccessToken())
                && !TextUtils.isEmpty(mainDataManager.getRefreshToken());
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
    public long getSchoolId() {
        return mainDataManager.getUserInfo().getSchoolId();
    }

    @Override
    public void getNoticeAmount() {
        addObserver(mainDataManager.getExtraInfo(), new NetworkObserver<ApiResult<PersonalExtraInfoDTO>>(false) {

            @Override
            public void onStart() {

            }

            @Override
            public void onReady(ApiResult<PersonalExtraInfoDTO> result) {
                if (null == result.getError()) {
                    if (!TextUtils.isEmpty(result.getData().getPreFileUrl())) {
                        Constant.IMAGE_PREFIX = result.getData().getPreFileUrl();
                    }
                    if (result.getData().getBanners() != null && result.getData().getBanners().size() > 0) {
                        for (BannerDTO banner : result.getData().getBanners()) {
                            if (banner.getLink().contains("?")) {
                                banner.setLink(banner.getLink() + "&accessToken="
                                        + mainDataManager.getAccessToken() + "&refreshToken=" + mainDataManager.getRefreshToken());
                            } else {
                                banner.setLink(banner.getLink() + "?accessToken="
                                        + mainDataManager.getAccessToken() + "&refreshToken=" + mainDataManager.getRefreshToken());
                            }
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

                    /**
                     * 记录h5服务中心的未读工单消息数量
                     */
                    int unReadWorkOrderRemarkMessageCount = result.getData().getUnReadWorkOrderRemarkMessageCount();
                    mainDataManager.saveUnReadWorkMessageCount(unReadWorkOrderRemarkMessageCount);


                    /**
                     * 新版本将报修记录转移为h5 用户端不需要显示小红点
                     */
//                    if (dto.getLastRepairTime() != null && mainDataManager.getLastRepairTime() < dto.getLastRepairTime()) {
//                        dto.setNeedShowDot(true);
//                        isShowRepair = true ;
//                    } else {
//                        isShowRepair = false ;
//                        dto.setNeedShowDot(false);
//                        dto.setLastRepairTime(null);
//                    }

                    if (result.getData().getShowTransfer() != null && result.getData().getShowTransfer()) {
                        mainDataManager.setNeedTransfer();
                        getMvpView().showXOkMigrate();
                    } else {
                        mainDataManager.setNotNeedTransfer();
                        getMvpView().hideXOkMigrate();
                    }
                    getMvpView().refreshProfile(dto);
                    if (result.getData().getNotifyAmount() != null) {
                        getMvpView().showNoticeAmount(result.getData().getNotifyAmount());
                    } else {
                        getMvpView().showNoticeAmount(0);
                    }

                    if (result.getData() != null) {
                        getMvpView().setCertificationStatus(result.getData());
                    }

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
        if (TextUtils.isEmpty(mainDataManager.getAccessToken()) || TextUtils.isEmpty(mainDataManager.getRefreshToken())) {
            return;
        }
        ReadNotifyReqDTO reqDTO = new ReadNotifyReqDTO();
        reqDTO.setId(id);
        addObserver(mainDataManager.readUrgentNotify(reqDTO), new NetworkObserver<ApiResult<BooleanRespDTO>>(false) {

            @Override
            public void onStart() {

            }

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
    public void gotoHeaterDevice(String defaultAddress, Long defaultSupplierId,
                                 String location, Long residenceId) {
        if (TextUtils.isEmpty(defaultAddress)) {
//            if (mainDataManager.getUserInfo().getResidenceId() == null
//                    || mainDataManager.getUserInfo().getResidenceId() == -1) {
//                getMvpView().showBindDormitoryDialog();
//            } else {
            getMvpView().showNoDeviceDialog();
//            }
        } else {
            getMvpView().gotoDevice(Device.HEATER, defaultAddress, defaultSupplierId,
                    location, residenceId, false);
        }
    }

    @Override
    public void getSchoolBusiness() {
        if (!getMvpView().isNetworkAvailable()) {
            List<BriefSchoolBusiness> businesses = mainDataManager.getSchoolBiz();
            if (businesses != null && businesses.isEmpty()) {
                getMvpView().showSchoolBiz(null);
            } else {
                getMvpView().showSchoolBiz(businesses);
            }
        } else {
            addObserver(mainDataManager.getSchoolBizList(), new NetworkObserver<ApiResult<QuerySchoolBizListRespDTO>>(false) {

                @Override
                public void onStart() {

                }

                @Override
                public void onReady(ApiResult<QuerySchoolBizListRespDTO> result) {
                    if (null == result.getError()) {
                        mainDataManager.setSchoolBiz(result.getData().getBusinesses());
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
    }

    @Override
    public void checkDeviceUsage(int type) {
        DeviceCheckReqDTO reqDTO = new DeviceCheckReqDTO();
        reqDTO.setDeviceType(type);
        addObserver(mainDataManager.checkDeviceUseage(reqDTO), new NetworkObserver<ApiResult<DeviceCheckRespDTO>>() {


            @Override
            public void onStart() {

            }

            @Override
            public void onReady(ApiResult<DeviceCheckRespDTO> result) {

                if (null == result.getError()) {
                    boolean isSetSex = (userDataManager.getUser().getSex() != null && (userDataManager.getUser().getSex() == 1 || userDataManager.getUser().getSex() == 2));
//                            boolean isSetDormitoryAddress = !TextUtils.isEmpty(userDataManager.getUser().getResidenceName());

                    if (!isSetSex) /*没有设置性别或是宿舍信息*/ {
                        getMvpView().gotoCompleteInfoActivity(getBathRouteRespDTO() ,result.getData());
                    } else {
                        mainDataManager.saveDeviceCategory(result.getData().getDevices());
                        getMvpView().showDeviceUsageDialog(type, result.getData());
                    }
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                    getMvpView().enableView();
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                getMvpView().enableView();
            }
        });
    }

    @Override
    public void checkUpdate(Integer code, String versionNo, String remindMobile) {
        CheckVersionUpdateReqDTO reqDTO = new CheckVersionUpdateReqDTO();
        reqDTO.setCode(code);
        reqDTO.setMobile(remindMobile);
        reqDTO.setVersionNo(versionNo);
        addObserver(mainDataManager.checkUpdate(reqDTO),
                new NetworkObserver<ApiResult<CheckVersionUpdateRespDTO>>(false) {


                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onReady(ApiResult<CheckVersionUpdateRespDTO> result) {
                        if (null == result.getError()) {
                            if (result.getData().getResult()) {
                                if (result.getData().getVersion().isMustUpdate()) {
                                    getMvpView().showUpdateDialog(result.getData().getVersion());
                                    mainDataManager.clearUpdateRemindTime();
                                } else {
                                    // 小于6小时不再提醒
                                    VersionDialogTime versionDialogTime = mainDataManager.getLastUpdateRemindTime();
                                    if (CommonUtil.canShowUpdateDialog(versionDialogTime, remindMobile)) {
                                        getMvpView().showUpdateDialog(result.getData().getVersion());
                                        mainDataManager.setLastUpdateRemindTime(remindMobile);
                                    }
                                }

                            }
                        }
                    }
                });
    }


    @Override
    public void setLastRepairTime(Long time) {
        mainDataManager.setLastRepairTime(time);
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
            public void onStart() {

            }

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

    @Override
    public void setCredits(Integer credits) {
        if (credits == null) {
            mainDataManager.setCredits(-1);
        } else {
            mainDataManager.setCredits(credits);
        }
    }

    @Override
    public Integer getCredits() {
        Integer credits = mainDataManager.getCredits();
        if (credits == null) {
            return -1;
        } else {
            return credits;
        }
    }

    @Override
    public void deletePushToken() {
        mainDataManager.deletePushToken();
    }

    @Override
    public String getPushToken() {
        return mainDataManager.getPushToken();
    }

    @Override
    public void setPushToken(String pushToken) {
        mainDataManager.setPushToken(pushToken);
    }

    @Override
    public void routeHeaterOrBathroom() {
        addObserver(mainDataManager.route(), new NetworkObserver<ApiResult<BathRouteRespDTO>>() {

            @Override
            public void onReady(ApiResult<BathRouteRespDTO> result) {
                if (null == result.getError()) {
                    setBathRouteRespDTO(result.getData());
                    if (!result.getData().isExistHistory()) {
                        //没有设置过洗澡地址，直接跳转到CompleteInfoActivity页面进行配置
                        getMvpView().gotoCompleteInfoActivity(result.getData());
//                        getMvpView().startToBathroomShower();
                    } else {
                        if (result.getData().isIsPubBath()) {
                            //设置了洗澡地址并且是公共浴室，判断是否设置了用户性别和宿舍信息
//                            没有设置用户性别
                            boolean isSetSex = (userDataManager.getUser().getSex() != null && (userDataManager.getUser().getSex() == 1 || userDataManager.getUser().getSex() == 2));
//                            boolean isSetDormitoryAddress = !TextUtils.isEmpty(userDataManager.getUser().getResidenceName());

                            if (!isSetSex) /*没有设置性别或是宿舍信息*/ {
                                getMvpView().gotoCompleteInfoActivity(result.getData());
                            } else {
                                getMvpView().routeToBathroomShower(result.getData());
                            }
                        } else {
                            getMvpView().routeToRoomShower(result.getData());
                        }
                    }
                } else {
                    getMvpView().enableView();
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                getMvpView().enableView();
            }
        });
    }


    @Override
    public void currentOrder() {
        addObserver(mainDataManager.currentOrder(), new NetworkObserver<ApiResult<CurrentBathOrderRespDTO>>() {

            @Override
            public void onReady(ApiResult<CurrentBathOrderRespDTO> result) {
                if (result.getError() == null) {
                    getMvpView().currentOrder(result.getData());
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public int getNoticeCount() {
        return noticeCount;
    }

    @Override
    public boolean getCommentEnable() {
        return mainDataManager.getCommentEnable();
    }

    @Override
    public boolean getIsShowRepair() {
        return isShowRepair;
    }

    @Override
    public boolean getIsFirstAfterLogin() {
        return mainDataManager.getIsFirstAfterLogin();
    }

    @Override
    public void setIsFirstAfterLogin(boolean b) {
        mainDataManager.setIsFirstAfterLogin(b);
    }

    @Override
    public void setCertifyStatus(int statusType) {
        userDataManager.setCertifyStatus(statusType);
    }

    @Override
    public void deleteFile() {
        rx.Observable.just(System.currentTimeMillis())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .map(aLong -> {
                    long lastDeleteTime = getLastDeleteTime();
                    // 一周删除日志文件
                    return aLong - lastDeleteTime > 24 * 60 * 60 * 1000 * 7;
                    // 测试
//                    return aLong - lastDeleteTime > 2 * 60;
                }).subscribe(aBoolean -> {
            if (aBoolean) {
                deleteLogFile();
                userDataManager.setDeleteFileTime(System.currentTimeMillis());
            }
        });
    }

    @Override
    public void rollingNotify() {
        addObserver(userDataManager.rollingNotify(), new NetworkObserver<ApiResult<RollingNotifyRespDTO>>() {

            @Override
            public void onReady(ApiResult<RollingNotifyRespDTO> result) {
                if (result.getError() == null) {
                    getMvpView().showRollingNotify(result.getData());
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public void getSchoolForumStatus() {
        addObserver(userDataManager.getSchoolForumStatus(), new NetworkObserver<ApiResult<SchoolForumStatusDTO>>() {

            @Override
            public void onReady(ApiResult<SchoolForumStatusDTO> result) {
                if (result.getError() == null) {
                    if (Constant.SCHOOL_FORUM_CLOSE.equals(result.getData().getSchoolForumStatus())) {
                        getMvpView().closeSchoolForum();
                    } else {
                        getMvpView().openSchoolForum();
                    }
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public String getRemindMobile() {
        return userDataManager.getRemindMobile();
    }


    private void deleteLogFile() {
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/xiaolian/" + getUserInfo().getId() + "/";
        File path = new File(filePath);
        if (!path.exists() && !path.mkdirs()) {
            return;
        }

        File outputImage = new File(filePath, DeviceLogFileName);
        try {
            if (outputImage.exists()) {
                FileUtils.deleteFile(outputImage);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }


    public long getLastDeleteTime() {
        return userDataManager.getLastDeleteTime();
    }

    @Override
    public void noticeCount() {
        addObserver(userDataManager.noticeCount(),
                new NetworkObserver<ApiResult<NoticeCountDTO>>(false, true) {


                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onReady(ApiResult<NoticeCountDTO> result) {
                        if (null == result.getError()) {
                            noticeCount = result.getData().getNoticeCount();
                            if (result.getData().getNoticeCount() != 0) {
                                getMvpView().showNoticeRemind();
                            } else {
                                getMvpView().hideNoticeRemind();
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
