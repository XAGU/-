package com.xiaolian.amigo.ui.user;

import com.xiaolian.amigo.data.manager.intf.IUserDataManager;
import com.xiaolian.amigo.data.vo.User;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.user.intf.IUserCertificationPresenter;
import com.xiaolian.amigo.ui.user.intf.IUserCertificationView;

import javax.inject.Inject;

public class UserCertificationPresenter <v extends IUserCertificationView> extends BasePresenter<v>
    implements IUserCertificationPresenter<v>{

    private IUserDataManager userDataManager ;


    @Inject
    public UserCertificationPresenter(IUserDataManager userDataManager ){
        super();
        this.userDataManager = userDataManager ;

    }

    @Override
    public User getUserInfo() {
        return userDataManager.getUser();
    }
}
