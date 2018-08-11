package com.xiaolian.amigo.ui.user;

import com.xiaolian.amigo.data.manager.intf.IUserDataManager;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.user.intf.ICompleteInfoPresenter;
import com.xiaolian.amigo.ui.user.intf.ICompleteInfoView;

import javax.inject.Inject;

public class CompleteInfoPresenter<V extends ICompleteInfoView>  extends BasePresenter<V>
             implements ICompleteInfoPresenter<V>{

    private IUserDataManager userDataManager;
    @Inject
    CompleteInfoPresenter(IUserDataManager userDataManager){
        this.userDataManager = userDataManager ;
    }

}
