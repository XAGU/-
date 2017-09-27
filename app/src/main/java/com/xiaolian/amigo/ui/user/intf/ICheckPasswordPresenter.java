package com.xiaolian.amigo.ui.user.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 校验密码
 * <p>
 * Created by zcd on 9/27/17.
 */

public interface ICheckPasswordPresenter<V extends ICheckPasswordView> extends IBasePresenter<V> {
    void checkPassword(String password);
}
