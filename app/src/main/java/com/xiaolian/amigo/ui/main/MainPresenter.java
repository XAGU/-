package com.xiaolian.amigo.ui.main;

import android.text.TextUtils;

import com.xiaolian.amigo.data.manager.intf.IMainDataManager;
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
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.device.dispenser.DispenserActivity;
import com.xiaolian.amigo.ui.device.heater.HeaterActivity;
import com.xiaolian.amigo.ui.main.intf.IMainPresenter;
import com.xiaolian.amigo.ui.main.intf.IMainView;

import javax.inject.Inject;

/**
 * 主页
 * <p>
 * Created by zcd on 9/20/17.
 */

public class MainPresenter<V extends IMainView> extends BasePresenter<V>
        implements IMainPresenter<V> {
    private static final String TAG = MainPresenter.class.getSimpleName();
    private IMainDataManager manager;

    @Inject
    public MainPresenter(IMainDataManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean isLogin() {
        return !TextUtils.isEmpty(manager.getToken());
    }

    @Override
    public void logout() {
        manager.logout();
    }

    @Override
    public User getUserInfo() {
        return manager.getUserInfo();
    }

    @Override
    public String getToken() {
        return manager.getToken();
    }

    @Override
    public void getNoticeAmount() {
        if (TextUtils.isEmpty(manager.getToken())) {
            return;
        }
        addObserver(manager.getExtraInfo(), new NetworkObserver<ApiResult<PersonalExtraInfoDTO>>(false) {

            @Override
            public void onReady(ApiResult<PersonalExtraInfoDTO> result) {
                if (null == result.getError()) {
                    if (result.getData().getBanners() != null && result.getData().getBanners().size() > 0) {
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
                        manager.setBonusAmount(result.getData().getBonusAmount());
                    }
                    getMvpView().showNoticeAmount(result.getData().getNotifyAmount());
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public void queryTimeValid(Integer deviceType, Class clz) {
        QueryTimeValidReqDTO reqDTO = new QueryTimeValidReqDTO();
        reqDTO.setDeviceType(deviceType);
        addObserver(manager.queryWaterTimeValid(reqDTO), new NetworkObserver<ApiResult<QueryTimeValidRespDTO>>() {

            @Override
            public void onReady(ApiResult<QueryTimeValidRespDTO> result) {
                if (null == result.getError()) {
                    if (result.getData().isValid()) {
//                        getMvpView().gotoDevice(clz);
                        if (deviceType == 1) {
                            getHeaterDeviceMacAddress();
                        } else if (deviceType == 2) {
                            getMvpView().gotoDevice(DispenserActivity.class);
                        }
                    } else {
                        getMvpView().showTimeValidDialog(result.getData().getTitle(),
                                result.getData().getRemark(), deviceType);
                    }
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public void readUrgentNotify(Long id) {
        if (TextUtils.isEmpty(manager.getToken())) {
            return;
        }
        ReadNotifyReqDTO reqDTO = new ReadNotifyReqDTO();
        reqDTO.setId(id);
        addObserver(manager.readUrgentNotify(reqDTO), new NetworkObserver<ApiResult<BooleanRespDTO>>(false) {

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
        return manager.isShowUrgencyNotify();
    }

    @Override
    public void setShowUrgencyNotify(boolean isShow) {
        manager.setShowUrgencyNotify(isShow);
    }

    @Override
    public void getHeaterDeviceMacAddress() {
        QueryDeviceListReqDTO reqDTO = new QueryDeviceListReqDTO();
        reqDTO.setSchoolId(manager.getUserInfo().getSchoolId());
        reqDTO.setType(1);
        if (manager.getUserInfo().getResidenceId() == null) {
            getMvpView().onError("请绑定默认宿舍");
            return;
        } else {
            reqDTO.setResidenceId(manager.getUserInfo().getResidenceId());
        }
        addObserver(manager.queryDeviceList(reqDTO), new NetworkObserver<ApiResult<QueryDeviceListRespDTO>>() {

            @Override
            public void onReady(ApiResult<QueryDeviceListRespDTO> result) {
                if (null == result.getError()) {
                    if (result.getData().getDevices() != null
                            && !result.getData().getDevices().isEmpty()
                            && result.getData().getDevices().get(0).getMacAddress() != null) {
                        getMvpView().gotoDevice(HeaterActivity.class,
                                result.getData().getDevices().get(0).getMacAddress(),
                                result.getData().getDevices().get(0).getLocation());
                    } else {
                        getMvpView().onError("设备不存在");
                    }
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public void getSchoolBusiness() {
        addObserver(manager.getSchoolBizList(), new NetworkObserver<ApiResult<QuerySchoolBizListRespDTO>>(false) {

            @Override
            public void onReady(ApiResult<QuerySchoolBizListRespDTO> result) {
                if (null == result.getError()) {
                    getMvpView().showSchoolBiz(result.getData().getBusinesses());
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public void getPrepayOrder() {
        OrderReqDTO reqDTO = new OrderReqDTO();
        // 查看未结束账单
        reqDTO.setOrderStatus(1);
        addObserver(manager.queryOrders(reqDTO), new NetworkObserver<ApiResult<OrderRespDTO>>(false) {
            @Override
            public void onReady(ApiResult<OrderRespDTO> result) {
                if (null == result.getError()) {
                    getMvpView().showPrepayOrder(result.getData().getOrders());
                }
            }
        });
    }

    @Override
    public void checkDeviceUsage(int type) {
        DeviceCheckReqDTO reqDTO = new DeviceCheckReqDTO();
        reqDTO.setDeviceType(type);
        addObserver(manager.checkDeviceUseage(reqDTO), new NetworkObserver<ApiResult<DeviceCheckRespDTO>>() {

            @Override
            public void onReady(ApiResult<DeviceCheckRespDTO> result) {
                if (null == result.getError()) {
                    getMvpView().showDeviceUsageDialog(type, result.getData());
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }


}
