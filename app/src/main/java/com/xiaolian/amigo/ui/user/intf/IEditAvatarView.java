package com.xiaolian.amigo.ui.user.intf;

import com.xiaolian.amigo.ui.base.intf.IBaseView;
import com.xiaolian.amigo.ui.user.adaptor.EditAvatarAdaptor;

import java.util.List;

/**
 * 更换头像
 *
 * @author zcd
 * @date 17/9/27
 */

public interface IEditAvatarView extends IBaseView {
    /**
     * 添加头像
     *
     * @param avatar 头像
     */
    void addAvatar(List<EditAvatarAdaptor.AvatarWrapper> avatar);

    /**
     * 设置头像
     *
     * @param pictureUrl 图片url
     */
    void setAvatar(String pictureUrl);

    /**
     * 结束页面
     */
    void finishView();
}
