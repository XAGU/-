package com.xiaolian.amigo.ui.login.intf;

import com.xiaolian.amigo.ui.base.intf.IBaseView;

/**
 * 找回密码
 *
 * @author zcd
 * @date 17/9/20
 */

public interface IPasswordRetrievalStep1View extends IBaseView {
    /**
     * 启动计时器
     */
    void startTimer();

    /**
     * 下一步
     */
    void next();
}
