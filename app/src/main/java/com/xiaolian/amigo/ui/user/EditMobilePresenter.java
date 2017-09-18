package com.xiaolian.amigo.ui.user;

import com.xiaolian.amigo.data.manager.intf.IUserDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.MobileUpdateReqDTO;
import com.xiaolian.amigo.data.network.model.dto.request.VerificationCodeGetReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.BooleanRespDTO;
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
                        getMvpView().showMessage("获取成功");
                        getMvpView().startTimer();
                    } else {
                        getMvpView().showMessage("获取失败");
                    }
                } else {
                    getMvpView().showMessage(result.getError().getDisplayMessage());
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
                    getMvpView().showMessage("修改成功");
                    getMvpView().finishView();
                } else {
                    getMvpView().showMessage(result.getError().getDisplayMessage());
                }
            }
        });
    }


}
