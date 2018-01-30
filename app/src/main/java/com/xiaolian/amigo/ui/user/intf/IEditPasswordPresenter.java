package com.xiaolian.amigo.ui.user.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 修改密码Presenter接口
 *
 * @author zcd
 * @date 17/9/15
 */
public interface IEditPasswordPresenter<V extends IEditPasswordView> extends IBasePresenter<V> {

    /**
     * 更新密码
     *
     * @param newPassword 新密码
     * @param oldPassword 旧密码
     */
    void updatePassword(String newPassword, String oldPassword);
}
