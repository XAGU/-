package com.xiaolian.amigo.ui.user;

import com.xiaolian.amigo.data.manager.intf.IUserDataManager;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.user.intf.IChangeBathroomPasswordPresenter;
import com.xiaolian.amigo.ui.user.intf.IChangeBathroomPasswordView;

import javax.inject.Inject;

public class ChangeBathroomPasswordPresenter<V extends IChangeBathroomPasswordView> extends BasePresenter<V>
        implements IChangeBathroomPasswordPresenter<V> {

    private IUserDataManager userDataManager;

    @Inject
    public ChangeBathroomPasswordPresenter(IUserDataManager userDataManager) {
        super();
        this.userDataManager = userDataManager;
    }




}
