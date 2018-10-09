package com.xiaolian.amigo.ui.user.intf;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 编辑个人信息Presenter
 *
 * @author zcd
 * @date 17/9/15
 */

public interface IEditProfilePresenter<V extends IEditProfileView> extends IBasePresenter<V> {
    /**
     * 获取个人信息
     */
    void getPersonProfile();

    /**
     * 修改学校校验
     */
    void checkChangeSchool();
    /**
     * 是否已经设置浴室密码
     */
    boolean isHadSetBathPassword();
}
