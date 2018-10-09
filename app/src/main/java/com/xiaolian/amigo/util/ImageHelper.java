package com.xiaolian.amigo.util;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.xiaolian.amigo.R;
import com.xiaolian.amigo.ui.model.CustomImageModelLoader;
import com.xiaolian.amigo.ui.model.CustomImageSizeModel;
import com.xiaolian.amigo.ui.model.CustomImageSizeModelImp;
import com.xiaolian.amigo.ui.widget.photoview.PhotoViewAttacher;

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

    public static void displayImage(Context context, final CustomImageSizeModel model, final ImageView imageView, final View loading) {
        loading.setVisibility(View.VISIBLE);
        Log.wtf(TAG ,"" + model.getBaseUrl());
//    DrawableRequestBuilder thumbnailBuilder = Glide
//            .with(imageView.getContext())
//            .load(new CustomImageSizeModelImp(model
//                    .getBaseUrl())
//                    .requestCustomSizeUrl(100, 50))
//            .skipMemoryCache(true)
//            .diskCacheStrategy(DiskCacheStrategy.NONE);

            Glide.with(context)
//            .using(new CustomImageModelLoader(imageView.getContext()))
//            .load(model)
////                    .load(model.getBaseUrl())
////                .centerCrop()
                    .load(model.getBaseUrl())
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            loading.setVisibility(View.GONE);
//                            PhotoViewAttacher attacher = new PhotoViewAttacher(imageView);
                            return false;
                        }
                    })
                    .into(imageView);




//                    .listener(new RequestListener<CustomImageSizeModel, GlideDrawable>() {
//        @Override
//        public boolean onException(Exception e, CustomImageSizeModel model, Target<GlideDrawable> target, boolean isFirstResource) {
//            return false;
//        }
//
//        @Override
//        public boolean onResourceReady(GlideDrawable resource, CustomImageSizeModel model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//            loading.setVisibility(View.GONE);
//            PhotoViewAttacher attacher = new PhotoViewAttacher(imageView);
////                            mAttacher.update();
//            return false;
//        }
//    })
//            .thumbnail(thumbnailBuilder)

}


}