package com.xiaolian.amigo.ui.user;

import com.xiaolian.amigo.data.manager.intf.IUserDataManager;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.user.intf.IEditPasswordPresenter;
import com.xiaolian.amigo.ui.user.intf.IEditPasswordView;

import javax.inject.Inject;

/**
 * 修改密码Presenter实现类
 * @author zcd
 */
public class EditPasswordPresenter<V extends IEditPasswordView> extends BasePresenter<V>
        implements IEditPasswordPresenter<V> {

    private IUserDataManager mUserDataManager;

    @Inject
    public EditPasswordPresenter(IUserDataManager manager) {
        super();
        mUserDataManager = manager;
    }
}
