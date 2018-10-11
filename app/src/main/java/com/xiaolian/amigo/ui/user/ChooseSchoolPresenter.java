package com.xiaolian.amigo.ui.user;

import com.xiaolian.amigo.data.manager.intf.IUserDataManager;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.user.intf.IChooseSchoolPresenter;
import com.xiaolian.amigo.ui.user.intf.IChooseSchoolView;

import javax.inject.Inject;

public class ChooseSchoolPresenter<v extends IChooseSchoolView> extends BasePresenter<v>
     implements IChooseSchoolPresenter<v>{


    private IUserDataManager userDataManager ;

    @Inject
    public ChooseSchoolPresenter(IUserDataManager userDataManager){
        this.userDataManager = userDataManager ;
    }
}
