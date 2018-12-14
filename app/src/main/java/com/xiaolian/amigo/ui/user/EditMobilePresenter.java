package com.xiaolian.amigo.ui.user;

import android.widget.Button;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.manager.intf.IUserDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.user.MobileUpdateReqDTO;
import com.xiaolian.amigo.data.network.model.login.VerificationCodeGetReqDTO;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.login.EntireUserDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.user.intf.IEditMobilePresenter;
import com.xiaolian.amigo.ui.user.intf.IEditMobileView;

import javax.inject.Inject;

/**
 * 修改手机号Presenter实现类
 *
 * @author zcd
 * @date 17/9/15
 */
public class EditMobilePresenter<V extends IEditMobileView> extends BasePresenter<V>
        implements IEditMobilePresenter<V> {

    private IUserDataManager userDataManager;

    @Inject
    EditMobilePresenter(IUserDataManager userDataManager) {
        super();
        this.userDataManager = userDataManager;
    }

    @Override
    public void getVerifyCode(String mobile , Button button) {
        VerificationCodeGetReqDTO dto = new VerificationCodeGetReqDTO();
        dto.setMobile(mobile);
        addObserver(userDataManager.getVerifyCode(dto), new NetworkObserver<ApiResult<BooleanRespDTO>>() {

            @Override
            public void onReady(ApiResult<BooleanRespDTO> result) {
                if (null == result.getError()) {
                    button.setEnabled(true);
                    if (result.getData().isResult()) {
                        getMvpView().onSuccess(R.string.get_success);
                        getMvpView().startTimer();
                    } else {
                        getMvpView().onError(R.string.get_fail);
                    }
                } else {
                    button.setEnabled(true);
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                button.setEnabled(true);
            }
        });
    }

    @Override
    public void updateMobile(String mobile, String code , Button button) {
        MobileUpdateReqDTO dto = new MobileUpdateReqDTO();
        dto.setMobile(mobile);
        dto.setCode(code);
        addObserver(userDataManager.updateMobile(dto), new NetworkObserver<ApiResult<EntireUserDTO>>() {

            @Override
            public void onReady(ApiResult<EntireUserDTO> result) {
                if (null == result.getError()) {
                    button.setEnabled(true);
                    getMvpView().onSuccess(R.string.change_mobile_success);
                    getMvpView().finishView();
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                    button.setEnabled(true);
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                button.setEnabled(true);
            }
        });
    }


}
