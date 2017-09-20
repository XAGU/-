package com.xiaolian.amigo.ui.user.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 修改密码Presenter接口
 * @author zcd
 */
public interface IEditPasswordPresenter<V extends IEditPasswordView> extends IBasePresenter<V> {

    void updatePassword(String newPassword, String oldPassword);
}
