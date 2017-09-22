package com.xiaolian.amigo.ui.main.intf;

import com.xiaolian.amigo.data.network.model.user.User;
import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 主页
 * <p>
 * Created by zcd on 9/20/17.
 */

public interface IMainPresenter<V extends IMainView> extends IBasePresenter<V> {
    boolean isLogin();

    User getUserInfo();

    void getNoticeAmount();
}
