package com.xiaolian.amigo.ui.user;

import com.xiaolian.amigo.data.manager.intf.IUserDataManager;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.base.intf.IBasePresenter;
import com.xiaolian.amigo.ui.user.intf.IFindBathroomPasswordPresenter;
import com.xiaolian.amigo.ui.user.intf.IFindBathroomPasswordView;

import javax.inject.Inject;

public class FindBathroomPasswordPresenter<V extends IFindBathroomPasswordView> extends BasePresenter<V>
implements IFindBathroomPasswordPresenter<V>{

    private IUserDataManager manager ;

    @Inject
    public FindBathroomPasswordPresenter(IUserDataManager manager){
        super();
        this.manager = manager ;
    }
}
