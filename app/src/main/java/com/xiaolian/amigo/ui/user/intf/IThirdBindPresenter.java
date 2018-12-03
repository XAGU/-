package com.xiaolian.amigo.ui.user.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

public interface IThirdBindPresenter <V extends IThirdBindView> extends IBasePresenter<V> {
    void unbind(int type);
}
