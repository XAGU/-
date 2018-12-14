package com.xiaolian.amigo.ui.user.intf;

import android.widget.Button;

import com.xiaolian.amigo.di.UserActivityContext;
import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 修改手机号Presenter接口
 *
 * @author zcd
 * @date 17/9/15
 */
@UserActivityContext
public interface IEditMobilePresenter<V extends IEditMobileView> extends IBasePresenter<V> {
    /**
     * 获取验证码
     *
     * @param mobile 手机号
     */
    void getVerifyCode(String mobile , Button button);

    /**
     * 更新手机号
     *
     * @param mobile 手机号
     * @param code   验证码
     */
    void updateMobile(String mobile, String code , Button button);
}
