package com.xiaolian.amigo.ui.more.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 关于我们
 * @author zcd
 * @date 17/11/9
 */

public interface IAboutUsPresenter<V extends IAboutUsView> extends IBasePresenter<V> {

    /**
     * 检查更新
     * @param code versionCode
     * @param versionNo versionName
     * @param click 是否是用户点击
     */
    void checkUpdate(Integer code, String versionNo, boolean click);
}
