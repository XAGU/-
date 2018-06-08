package com.xiaolian.amigo.ui.more.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 更多
 *
 * @author zcd
 * @date 17/10/13
 */

public interface IMorePresenter<V extends IMoreView> extends IBasePresenter<V> {
    /**
     * 退出登录
     */
    void logout();

    /**
     * 获取token
     *
     * @return token
     */
    String getToken();

    Long getUserId();

    void deletePushToken();

    Long getSchoolId();

    String getPushTag();

    void setPushTag(String pushTag);
}
