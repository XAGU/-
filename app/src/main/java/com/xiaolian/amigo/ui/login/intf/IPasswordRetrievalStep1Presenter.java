package com.xiaolian.amigo.ui.login.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 找回密码
 *
 * @author zcd
 * @date 17/9/20
 */

public interface IPasswordRetrievalStep1Presenter<V extends IPasswordRetrievalStep1View> extends IBasePresenter<V> {
    /**
     * 获取验证码
     *
     * @param mobile 手机号
     */
    void getVerification(String mobile);

    /**
     * 校验验证码
     *
     * @param mobile 手机号
     * @param code   验证码
     */
    void checkVerification(String mobile, String code);
}
