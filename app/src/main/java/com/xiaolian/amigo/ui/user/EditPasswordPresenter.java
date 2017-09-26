package com.xiaolian.amigo.ui.user;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.manager.intf.IUserDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.dto.request.PasswordUpdateReqDTO;
import com.xiaolian.amigo.data.network.model.dto.response.SimpleRespDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.user.intf.IEditPasswordPresenter;
import com.xiaolian.amigo.ui.user.intf.IEditPasswordView;

import javax.inject.Inject;

/**
 * 修改密码Presenter实现类
 * @author zcd
 */
public class EditPasswordPresenter<V extends IEditPasswordView> extends BasePresenter<V>
        implements IEditPasswordPresenter<V> {

    private IUserDataManager manager;

    @Inject
    public EditPasswordPresenter(IUserDataManager manager) {
        super();
        this.manager = manager;
    }

    @Override
    public void updatePassword(String newPassword, String oldPassword) {
        PasswordUpdateReqDTO dto = new PasswordUpdateReqDTO();
        dto.setNewPassword(newPassword);
        dto.setOldPassword(oldPassword);
        addObserver(manager.updatePassword(dto), new NetworkObserver<ApiResult<SimpleRespDTO>>() {

            @Override
            public void onReady(ApiResult<SimpleRespDTO> result) {
                if (null == result.getError()) {
                    getMvpView().onSuccess(R.string.change_success);
                    getMvpView().finishView();
                } else {
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });
    }
}
