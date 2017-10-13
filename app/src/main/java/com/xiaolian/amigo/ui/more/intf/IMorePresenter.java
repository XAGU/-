package com.xiaolian.amigo.ui.more.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 更多
 * <p>
 * Created by zcd on 10/13/17.
 */

public interface IMorePresenter<V extends IMoreView> extends IBasePresenter<V> {
    void logout();
}
