package com.xiaolian.amigo.ui.main.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 闪屏页
 * <p>
 * Created by zcd on 17/11/13.
 */

public interface ISplashPresenter<V extends ISplashView> extends IBasePresenter<V> {

    void getNoticeAmount();
}
