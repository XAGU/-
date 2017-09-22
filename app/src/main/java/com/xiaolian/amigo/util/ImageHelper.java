package com.xiaolian.amigo.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * ImageHelper
 * <p>
 * Created by zcd on 9/22/17.
 */

public class ImageHelper {
    public static final String TAG = ImageHelper.class.getSimpleName();

    // public static final int DEFAULT_AVATAR_THUMB_SIZE = ScreenUtil.getDimension(R.dimen.avatar_max_size);
    // public static final int DEFAULT_AVATAR_NOTIFICATION_ICON_SIZE = ScreenUtil.getDimension(R.dimen.avatar_notification_size);

    /**
     * 相册大图
     *
     * @param context
     * @param imageView
     * @param path
     */
    public static void loadAlbum(Context context, ImageView imageView, String path) {
        if (context == null) {
            return;
        }
        Glide.with(context).load(path).into(imageView);
    }

}