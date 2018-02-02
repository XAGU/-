package com.xiaolian.amigo.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.xiaolian.amigo.R;

/**
 * ImageHelper
 *
 * @author zcd
 * @date 17/9/22
 */

public class ImageHelper {
    public static final String TAG = ImageHelper.class.getSimpleName();

    // public static final int DEFAULT_AVATAR_THUMB_SIZE = ScreenUtil.getDimension(R.dimen.avatar_max_size);
    // public static final int DEFAULT_AVATAR_NOTIFICATION_ICON_SIZE = ScreenUtil.getDimension(R.dimen.avatar_notification_size);

    /**
     * 相册大图
     *
     * @param context   context
     * @param imageView imageView
     * @param path      path
     */
    public static void loadAlbum(Context context, ImageView imageView, String path) {
        if (context == null) {
            return;
        }
        Glide.with(context).load(path)
                .asBitmap()
                .placeholder(R.drawable.ic_picture_error)
                .error(R.drawable.ic_picture_error)
                .into(imageView);
    }

}