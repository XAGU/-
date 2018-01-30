package com.xiaolian.amigo.ui.lostandfound.intf;

import android.content.Context;
import android.net.Uri;

import com.xiaolian.amigo.data.enumeration.OssFileType;
import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

import java.util.List;

/**
 * 失物发布
 *
 * @author zcd
 * @date 17/9/21
 */

public interface IPublishLostPresenter<V extends IPublishLostView> extends IBasePresenter<V> {
    /**
     * 发布失物招领
     *
     * @param desc     描述
     * @param images   图片
     * @param itemName 物品名称
     * @param location 地点
     * @param lostTime 时间
     * @param mobile   手机
     * @param title    标题
     * @param type     类型
     */
    void publishLostAndFound(String desc, List<String> images, String itemName, String location,
                             Long lostTime, String mobile, String title, Integer type);

    /**
     * 上传图片
     *
     * @param context  上下文
     * @param imageUri 图片路径
     * @param position 索引
     * @param type     类型
     */
    void uploadImage(Context context, Uri imageUri, int position, OssFileType type);
}
