package com.xiaolian.amigo.ui.login.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 找回密码
 * <p>
 * Created by zcd on 9/20/17.
 */

public interface IPasswordRetrievalStep1Presenter<V extends IPasswordRetrievalStep1View> extends IBasePresenter<V> {
    void getVerification(String mobile);

    // 校验验证码
    void checkVerification(String mobile, String code);
}
