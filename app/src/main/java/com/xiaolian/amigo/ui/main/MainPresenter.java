package com.xiaolian.amigo.ui.main;

import android.text.TextUtils;

import com.xiaolian.amigo.data.manager.intf.IMainDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.response.PersonalExtraInfoDTO;
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
    public User getUserInfo() {
        return manager.getUserInfo();
    }

    @Override
    public void getNoticeAmount() {
        addObserver(manager.getExtraInfo(), new NetworkObserver<ApiResult<PersonalExtraInfoDTO>>() {

            @Override
            public void onReady(ApiResult<PersonalExtraInfoDTO> result) {
                if (null == result.getError()) {
                    getMvpView().showNoticeAmount(result.getData().getNotifyAmount());
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }


}
