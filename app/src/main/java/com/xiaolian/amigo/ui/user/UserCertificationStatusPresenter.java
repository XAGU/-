package com.xiaolian.amigo.ui.user;

import com.xiaolian.amigo.data.manager.intf.IUserDataManager;
import com.xiaolian.amigo.data.network.model.ApiResult;
import com.xiaolian.amigo.data.network.model.user.UserCertifyInfoRespDTO;
import com.xiaolian.amigo.data.vo.User;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.user.intf.IUserCerticifationStatusPresenter;
import com.xiaolian.amigo.ui.user.intf.IUserCertificationStatusView;

import java.util.Base64;

import javax.inject.Inject;

public class UserCertificationStatusPresenter<v extends IUserCertificationStatusView> extends BasePresenter<v>
     implements IUserCerticifationStatusPresenter<v> {


    private IUserDataManager userDataManager ;

    protected String dormitory ;

    @Inject
    public UserCertificationStatusPresenter(IUserDataManager userDataManager){
        this.userDataManager = userDataManager ;
    }


    @Override
    public void getCertifyInfo() {
        addObserver(userDataManager.certifyInfo() ,new NetworkObserver<ApiResult<UserCertifyInfoRespDTO>>(){

            @Override
            public void onReady(ApiResult<UserCertifyInfoRespDTO> result) {
                if (result.getError() == null){
                    getMvpView().setInfo(result.getData());
                }else{
                    getMvpView().onError(result.getError().getDisplayMessage());
                }
            }
        });


    }


    @Override
    public void  getDormitory() {
        User user =userDataManager.getUser();
        dormitory = user.getDormitory() ;
    }

    @Override
    public String getDormInfo() {
        return dormitory;
    }


}
