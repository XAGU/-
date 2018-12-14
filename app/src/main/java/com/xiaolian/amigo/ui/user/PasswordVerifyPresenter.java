package com.xiaolian.amigo.ui.user;

import android.widget.Button;

import com.xiaolian.amigo.R;
import com.xiaolian.amigo.data.manager.intf.IUserDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.user.PasswordCheckReqDTO;
import com.xiaolian.amigo.data.network.model.user.PasswordVerifyRespDTO;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.user.intf.IPasswordVerifyPresenter;
import com.xiaolian.amigo.ui.user.intf.IPasswordVerifyView;

import javax.inject.Inject;

public class PasswordVerifyPresenter <V extends IPasswordVerifyView> extends BasePresenter<V>
        implements IPasswordVerifyPresenter<V> {

    private IUserDataManager userDataManager;

    @Inject
    PasswordVerifyPresenter(IUserDataManager userDataManager) {
        super();
        this.userDataManager = userDataManager;
    }

    @Override
    public void verifyPassword(String passsword , Button button) {
        PasswordCheckReqDTO dto = new PasswordCheckReqDTO();
        dto.setPassword(passsword);
        addObserver(userDataManager.verifyPassword(dto), new NetworkObserver<ApiResult<PasswordVerifyRespDTO>>() {
            @Override
            public void onReady(ApiResult<PasswordVerifyRespDTO> result) {
                if(null == result.getError()){
                    button.setEnabled(true);
                    if(result.getData().isResult()){
                        //密码验证成功
                        getMvpView().goToChangeView();
                    }else if(!result.getData().isResult() && null != result.getData().getRemaining()) {
                        Integer remain = result.getData().getRemaining();
                        //检查密码错误剩余次数
                        if (1 == remain || 2 == remain) {
                            PasswordVerifyActivity activity = (PasswordVerifyActivity) getMvpView();
                            String title =activity.getResources().getString(R.string.verify_password_only_one_titile,remain);
                            getMvpView().showTipDialog(title,activity.getString(R.string.verify_password_tip));
                        }

                    }else if(!result.getData().isResult() && null != result.getData().getProtectInMinutes()){
                        //检查剩余分钟数
                        PasswordVerifyActivity activity = (PasswordVerifyActivity) getMvpView();
                        int rest = result.getData().getProtectInMinutes();
                        String title =activity.getResources().getString(R.string.verify_password_failed_title,rest);
                        getMvpView().showTipDialog(title,activity.getString(R.string.verify_password_failed));

                    }else{
                        getMvpView().onError("请输入正确的登录密码");

                    }
                }else{
                    button.setEnabled(true);
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                getMvpView().onError("网络错误");
                button.setEnabled(true);
            }
        });
    }
}

