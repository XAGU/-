package com.xiaolian.amigo.ui.user.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

public interface IPasswordVerifyPresenter <V extends IPasswordVerifyView> extends IBasePresenter<V> {
    void verifyPassword(String passsword);
}
