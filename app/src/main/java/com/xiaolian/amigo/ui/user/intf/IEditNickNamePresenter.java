package com.xiaolian.amigo.ui.user.intf;

import com.xiaolian.amigo.di.UserActivityContext;
import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 编辑昵称Presenter接口
 *
 * @author zcd
 * @date 17/9/15
 */
@UserActivityContext
public interface IEditNickNamePresenter<V extends IEditNickNameView> extends IBasePresenter<V> {
    /**
     * 更新昵称
     *
     * @param nickName 昵称
     */
    void updateNickName(String nickName);
}
