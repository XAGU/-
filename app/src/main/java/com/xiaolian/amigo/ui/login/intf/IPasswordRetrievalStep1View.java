package com.xiaolian.amigo.ui.login.intf;

import com.xiaolian.amigo.ui.base.intf.IBaseView;

/**
 * 找回密码
 * <p>
 * Created by zcd on 9/20/17.
 */

public interface IPasswordRetrievalStep1View extends IBaseView{
    void startTimer();

    void next();
}
