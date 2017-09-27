package com.xiaolian.amigo.ui.user.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 更换头像
 * <p>
 * Created by zcd on 9/27/17.
 */

public interface IEditAvatarPresenter<V extends IEditAvatarVIew> extends IBasePresenter<V> {
    void getAvatarList();
}
