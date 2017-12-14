package com.xiaolian.amigo.ui.user;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.manager.intf.IUserDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.MobileUpdateReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.VerificationCodeGetReqDTO;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.dto.response.EntireUserDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.user.intf.IEditMobilePresenter;
import com.xiaolian.amigo.ui.user.intf.IEditMobileView;

import javax.inject.Inject;

/**
 * 修改手机号Presenter实现类
 * @author zcd
 */
public class EditMobilePresenter<V extends IEditMobileView> extends BasePresenter<V>
        implements IEditMobilePresenter<V> {

    private IUserDataManager manager;

    @Inject
    public EditMobilePresenter(IUserDataManager manager) {
        super();
        this.manager = manager;
    }

    @Override
    public void getVerifyCode(String mobile) {
        VerificationCodeGetReqDTO dto = new VerificationCodeGetReqDTO();
        dto.setMobile(mobile);
        addObserver(manager.getVerifyCode(dto), new NetworkObserver<ApiResult<BooleanRespDTO>>() {

            @Override
            public void onReady(ApiResult<BooleanRespDTO> result) {
                if (null == result.getError()) {
                    if (result.getData().isResult()) {
                        getMvpView().onSuccess(R.string.get_success);
                        getMvpView().startTimer();
                    } else {
                        getMvpView().onError(R.string.get_fail);
                    }
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }

    @Override
    public void updateMobile(String mobile, String code) {
        MobileUpdateReqDTO dto = new MobileUpdateReqDTO();
        dto.setMobile(mobile);
        dto.setCode(code);
        addObserver(manager.updateMobile(dto), new NetworkObserver<ApiResult<EntireUserDTO>>() {

            @Override
            public void onReady(ApiResult<EntireUserDTO> result) {
                if (null == result.getError()) {
                    getMvpView().onSuccess(R.string.change_mobile_success);
                    getMvpView().finishView();
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }


}
