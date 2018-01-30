package com.xiaolian.amigo.ui.login.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 找回密码2
 *
 * @author zcd
 * @date 17/9/20
 */

public interface IPasswordRetrievalStep2Presenter<V extends IPasswordRetrievalStep2View> extends IBasePresenter<V> {
    /**
     * 重置密码
     *
     * @param code     验证码
     * @param mobile   手机号
     * @param password 密码
     */
    void resetPassword(String code, String mobile, String password);
}
