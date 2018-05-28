package com.xiaolian.amigo.ui.lostandfound.intf;

import android.content.Context;
import android.net.Uri;

import com.xiaolian.amigo.data.enumeration.OssFileType;
import com.xiaolian.amigo.ui.base.intf.IBasePresenter;
import com.xiaolian.amigo.ui.lostandfound.PublishLostAndFoundActivity;

import java.util.List;

/**
 * @author zcd
 * @date 18/5/14
 */
public interface IPublishLostAndFoundPresenter<V extends IPublishLostAndFoundView>
    extends IBasePresenter<V> {
    void uploadImage(Context activity, Uri imageUri, int position, OssFileType found);

    void publishLostAndFound(String desc, List<String> images, String title, int type);
}
