package com.xiaolian.amigo.ui.user;

import com.xiaolian.amigo.data.manager.intf.IUserDataManager;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.user.intf.IUserCerticifationStatusPresenter;
import com.xiaolian.amigo.ui.user.intf.IUserCertificationStatusView;

import javax.inject.Inject;

public class UserCertificationStatusPresenter<v extends IUserCertificationStatusView> extends BasePresenter<v>
     implements IUserCerticifationStatusPresenter<v> {


    private IUserDataManager userDataManager ;


    @Inject
    public UserCertificationStatusPresenter(IUserDataManager userDataManager){
        this.userDataManager = userDataManager ;
    }
}
