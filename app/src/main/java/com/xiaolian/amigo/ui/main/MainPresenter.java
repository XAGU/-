package com.xiaolian.amigo.ui.main;

import android.text.TextUtils;

import com.xiaolian.amigo.data.manager.intf.IMainDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.QueryTimeValidReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.ReadNotifyReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.PersonalExtraInfoDTO;
import com.xiaolian.amigo.data.network.model.dto.response.QueryTimeValidRespDTO;
import com.xiaolian.amigo.data.network.model.user.User;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.main.intf.IMainPresenter;
import com.xiaolian.amigo.ui.main.intf.IMainView;

import javax.inject.Inject;

/**
 * 主页
 * <p>
 * Created by zcd on 9/20/17.
 */

public class MainPresenter<V extends IMainView> extends BasePresenter<V> implements IMainPresenter<V> {
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
        manager.setToken("");
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
        addObserver(manager.getExtraInfo(), new NetworkObserver<ApiResult<PersonalExtraInfoDTO>>() {

            @Override
            public void onReady(ApiResult<PersonalExtraInfoDTO> result) {
                if (null == result.getError()) {
                    if (result.getData().getUrgentNotify() != null) {
                        getMvpView().showUrgentNotify(result.getData().getUrgentNotify().getContent(),
                                result.getData().getUrgentNotify().getId());
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
                        getMvpView().gotoDevice(clz);
                    } else {
                        getMvpView().showTimeValidDialog(result.getData().getTitle(),
                                result.getData().getRemark(), clz);
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
        addObserver(manager.readUrgentNotify(reqDTO), new NetworkObserver<ApiResult<BooleanRespDTO>>() {

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


}
