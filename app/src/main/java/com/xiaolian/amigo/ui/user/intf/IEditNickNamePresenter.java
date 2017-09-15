package com.xiaolian.amigo.ui.user.intf;

import com.xiaolian.amigo.di.UserActivityContext;
import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 编辑昵称Presenter接口
 * @author zcd
 */
@UserActivityContext
public interface IEditNickNamePresenter<V extends IEditNickNameView> extends IBasePresenter<V> {
    void updateNickName(String nickName);
}
