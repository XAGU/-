package com.xiaolian.amigo.ui.user.intf;

import com.xiaolian.amigo.ui.base.intf.IBaseView;
import com.xiaolian.amigo.ui.user.adaptor.EditAvatarAdaptor;

import java.util.List;

/**
 * 更换头像
 * <p>
 * Created by zcd on 9/27/17.
 */

public interface IEditAvatarVIew extends IBaseView {
    void addAvatar(List<EditAvatarAdaptor.AvatarWrapper> avatar);
}
