package com.xiaolian.amigo.ui.user;

import com.xiaolian.amigo.data.manager.intf.IUserDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.user.PasswordCheckReqDTO;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.user.intf.ICheckPasswordPresenter;
import com.xiaolian.amigo.ui.user.intf.ICheckPasswordView;

import javax.inject.Inject;

/**
 * 校验密码
 * <p>
 * Created by zcd on 9/27/17.
 */

public class CheckPasswordPresenter<V extends ICheckPasswordView> extends BasePresenter<V>
        implements ICheckPasswordPresenter<V> {

    private IUserDataManager userDataManager;

    @Inject
    public CheckPasswordPresenter(IUserDataManager userDataManager) {
        super();
        this.userDataManager = userDataManager;
    }

    @Override
    public void checkPassword(String password) {
        PasswordCheckReqDTO reqDTO = new PasswordCheckReqDTO();
        reqDTO.setPassword(password);
        addObserver(userDataManager.checkPasswordValid(reqDTO), new NetworkObserver<ApiResult<BooleanRespDTO>>() {

            @Override
            public void onReady(ApiResult<BooleanRespDTO> result) {
                if (null == result.getError()) {
                    if (result.getData().isResult()) {
                        getMvpView().gotoChangeMobile();
                    } else {
                        getMvpView().onError("密码错误，请重新输入");
                    }
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }
}
