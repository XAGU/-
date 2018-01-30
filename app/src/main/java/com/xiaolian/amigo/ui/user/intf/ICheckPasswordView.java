package com.xiaolian.amigo.ui.user.intf;

import com.xiaolian.amigo.ui.base.intf.IBaseView;

/**
 * 修改手机号时，先校验密码
 *
 * @author zcd
 * @date 17/9/27
 */

public interface ICheckPasswordView extends IBaseView {
    /**
     * 跳转到修改手机号页面
     */
    void gotoChangeMobile();
}
