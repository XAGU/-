package com.xiaolian.amigo.ui.user;

import com.xiaolian.amigo.data.manager.intf.IUserDataManager;
import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.user.intf.IEditMobilePresenter;
import com.xiaolian.amigo.ui.user.intf.IEditMobileView;

import javax.inject.Inject;

/**
 * 修改手机号Presenter实现类
 * @author zcd
 */
public class EditMobilePresenter<V extends IEditMobileView> extends BasePresenter<V>
        implements IEditMobilePresenter<V> {

    private IUserDataManager mUserDataManager;

    @Inject
    public EditMobilePresenter(IUserDataManager manager) {
        super();
        mUserDataManager = manager;
    }
}
