package com.xiaolian.amigo.ui.user;

import android.widget.Button;

import com.xiaolian.amigo.data.manager.intf.IUserDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.bathroom.BathPasswordUpdateReqDTO;
import com.xiaolian.amigo.data.network.model.common.BooleanRespDTO;
import com.xiaolian.amigo.data.network.model.common.SimpleRespDTO;
import com.xiaolian.amigo.data.network.model.login.VerificationCodeCheckReqDTO;
import com.xiaolian.amigo.data.network.model.login.VerificationCodeGetReqDTO;
import com.xiaolian.amigo.data.vo.User;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.user.intf.IFindBathroomPasswordPresenter;
import com.xiaolian.amigo.ui.user.intf.IFindBathroomPasswordView;

import java.util.List;

import javax.inject.Inject;

public class FindBathroomPasswordPresenter<V extends IFindBathroomPasswordView> extends BasePresenter<V>
implements IFindBathroomPasswordPresenter<V>{

    private IUserDataManager manager ;
    private String code ;
    private String mobile ;

    @Inject
    public FindBathroomPasswordPresenter(IUserDataManager manager){
        super();
        this.manager = manager ;
    }


    @Override
    public void updateBathroomPassword(String password , Button button ) {

        BathPasswordUpdateReqDTO  reqDTO = new BathPasswordUpdateReqDTO();
        reqDTO.setBathPassword(password);
        reqDTO.setCode(code);
        reqDTO.setMobile(mobile);
        addObserver(manager.updateBathroomPassword(reqDTO),new NetworkObserver<ApiResult<SimpleRespDTO>>(){

            @Override
            public void onReady(ApiResult<SimpleRespDTO> result) {
                button.setEnabled(true);
                if(null == result.getError() ){
                    getMvpView().onSuccess("浴室密码更新成功");
                    manager.setBathroomPassword();
                    getMvpView().backToCenter();
                }else{
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
    public String  getMobile(){
        User user = null ;
        if (this.manager != null){
            user = manager.getUser() ;
        }
        if (user != null){
            return user.getMobile();
        }else{
            return "";
        }

    }

    @Override
    public void getVerifyCode(String mobile , Button button) {
        VerificationCodeGetReqDTO reqDTO = new VerificationCodeGetReqDTO();
        reqDTO.setMobile(mobile);
        addObserver(manager.getVerification(reqDTO),
                new NetworkObserver<ApiResult<BooleanRespDTO>>() {

                    @Override
                    public void onReady(ApiResult<BooleanRespDTO> result) {
                        button.setEnabled(true);
                        if (null == result.getError()) {
                            getMvpView().onSuccess("验证码发送成功");
                            getMvpView().startTimer();
                        } else {
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
    public void checkVerifyCode(String mobile, String code , Button button) {
        VerificationCodeCheckReqDTO reqDTO = new VerificationCodeCheckReqDTO();
        reqDTO.setCode(code);
        reqDTO.setMobile(mobile);
        addObserver(manager.checkVerifyCode(reqDTO),
                new NetworkObserver<ApiResult<BooleanRespDTO>>() {

                    @Override
                    public void onReady(ApiResult<BooleanRespDTO> result) {
                        button.setEnabled(true);
                        if (null == result.getError()) {
                            getMvpView().nextStep();
                            FindBathroomPasswordPresenter.this.code = code ;
                            FindBathroomPasswordPresenter.this.mobile = mobile ;
                        } else {
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
    public List<String> getBathroomPasswordDesc() {
        return manager.getBathroomPasswordDesc();
    }
}
