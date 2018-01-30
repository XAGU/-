package com.xiaolian.amigo.ui.user.intf;

import android.content.Context;
import android.net.Uri;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

/**
 * 更换头像
 *
 * @author zcd
 * @date 17/9/27
 */

public interface IEditAvatarPresenter<V extends IEditAvatarView> extends IBasePresenter<V> {
    /**
     * 获取头像列表
     */
    void getAvatarList();

    /**
     * 上传图片
     *
     * @param context  上下文
     * @param imageUri 图片路径
     */
    void uploadImage(Context context, Uri imageUri);

    /**
     * 上传头像
     *
     * @param avatarUrl 头像链接
     */
    void updateAvatarUrl(String avatarUrl);
}
