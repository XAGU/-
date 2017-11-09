package com.xiaolian.amigo.ui.more.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 关于我们
 * <p>
 * Created by zcd on 17/11/9.
 */

public interface IAboutUsPresenter<V extends IAboutUsView> extends IBasePresenter<V> {

    void checkUpdate(Integer code, String versionNo, boolean click);
}
