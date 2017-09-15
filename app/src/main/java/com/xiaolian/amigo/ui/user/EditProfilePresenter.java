package com.xiaolian.amigo.ui.user;

import com.xiaolian.amigo.ui.base.BasePresenter;
import com.xiaolian.amigo.ui.user.intf.IEditProfilePresenter;
import com.xiaolian.amigo.ui.user.intf.IEditProfileView;

import javax.inject.Inject;

/**
 * EditProfilePresenter实现类
 * @author zcd
 */

public class EditProfilePresenter<V extends IEditProfileView> extends BasePresenter<V>
        implements IEditProfilePresenter<V> {

    @Inject
    public EditProfilePresenter() {
        super();
    }
}
