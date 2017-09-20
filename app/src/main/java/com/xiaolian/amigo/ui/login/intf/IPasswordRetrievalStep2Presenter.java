package com.xiaolian.amigo.ui.login.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 找回密码2
 * <p>
 * Created by zcd on 9/20/17.
 */

public interface IPasswordRetrievalStep2Presenter<V extends IPasswordRetrievalStep2View> extends IBasePresenter<V> {
    void resetPassword(String code, String mobile, String password);
}
