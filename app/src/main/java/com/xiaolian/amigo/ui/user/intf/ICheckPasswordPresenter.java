package com.xiaolian.amigo.ui.user.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 校验密码
 *
 * @author zcd
 * @date 17/9/27
 */

public interface ICheckPasswordPresenter<V extends ICheckPasswordView> extends IBasePresenter<V> {
    /**
     * 检查密码
     *
     * @param password 密码
     */
    void checkPassword(String password);
}
