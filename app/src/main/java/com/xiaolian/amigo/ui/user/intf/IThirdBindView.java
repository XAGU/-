package com.xiaolian.amigo.ui.user.intf;

import com.xiaolian.amigo.ui.base.intf.IBaseView;
import com.xiaolian.amigo.ui.user.EditProfileActivity;

public interface IThirdBindView extends IBaseView {

    void setNickName(String nickName);

    void gotoEditProfile(EditProfileActivity.Event.EventType type);
}
