package com.xiaolian.amigo.ui.user;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.manager.intf.IUserDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.user.PasswordUpdateReqDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleRespDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.login.LoginActivity;
import com.xiaolian.amigo.ui.user.intf.IEditPasswordPresenter;
import com.xiaolian.amigo.ui.user.intf.IEditPasswordView;

import javax.inject.Inject;

/**
 * 修改密码Presenter实现类
 *
 * @author zcd
 * @date 17/9/15
 */
public class EditPasswordPresenter<V extends IEditPasswordView> extends BasePresenter<V>
        implements IEditPasswordPresenter<V> {

    private IUserDataManager userDataManager;

    @Inject
    EditPasswordPresenter(IUserDataManager userDataManager) {
        super();
        this.userDataManager = userDataManager;
    }

    @Override
    public void updatePassword(String newPassword, String oldPassword) {
        PasswordUpdateReqDTO dto = new PasswordUpdateReqDTO();
        dto.setNewPassword(newPassword);
        dto.setOldPassword(oldPassword);
        addObserver(userDataManager.updatePassword(dto), new NetworkObserver<ApiResult<SimpleRespDTO>>() {

            @Override
            public void onReady(ApiResult<SimpleRespDTO> result) {
                if (null == result.getError()) {
                    if (result.getData().getResult()) {
                        getMvpView().onSuccess(R.string.change_password_success);
                        getMvpView().finishView();

                    } else if (!result.getData().getResult() && result.getData().getRemaining() != null) {
                        //检查密码错误剩余次数
                        if (1 == result.getData().getRemaining()) {
                            LoginActivity activity = (LoginActivity) getMvpView();
                            getMvpView().showTipDialog(activity.getString(R.string.verify_password_only_one_titile), activity.getString(R.string.verify_password_tip));
                        }
                    } else if (!result.getData().getResult() && result.getData().getProtectInMinutes() != null) {
                        //检查剩余分钟数
                        LoginActivity activity = (LoginActivity) getMvpView();
                        int rest = result.getData().getProtectInMinutes();
                        String title = activity.getResources().getString(R.string.verify_password_failed_title, rest);
                        getMvpView().showTipDialog(title, activity.getString(R.string.verify_password_failed_stop));
                    } else {
                        getMvpView().onError("请输入正确的登录密码");
                    }
                }else{
                    getMvpView().onError(result.getError().getDisplayMessage());
                }

            }
        });
    }
}
