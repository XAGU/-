package com.xiaolian.amigo.ui.user.intf;

import android.net.Uri;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 编辑个人信息Presenter
 * @author zcd
 */

public interface IEditProfilePresenter <V extends IEditProfileView> extends IBasePresenter<V> {
    void getPersonProfile();

    void uploadImage(Uri imageUri);
}
