package com.xiaolian.amigo.ui.lostandfound.intf;

import android.net.Uri;

import com.xiaolian.amigo.ui.base.intf.IBasePresenter;

import java.util.List;

/**
 * 失物发布
 * <p>
 * Created by zcd on 9/21/17.
 */

public interface IPublishLostPresenter<V extends IPublishLostView> extends IBasePresenter<V> {
    void publishLostAndFound(String desc, List<String> images, String itemName, String location,
                             String lostTime, String mobile, String title, Integer type);

    void uploadImage(Uri imageUri);
}
